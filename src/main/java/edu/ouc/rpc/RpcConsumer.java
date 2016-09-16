package edu.ouc.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import edu.ouc.rpc.aop.ConsumerHook;
import edu.ouc.rpc.async.ResponseCallbackListener;
import edu.ouc.rpc.async.ResponseFuture;
import edu.ouc.rpc.context.RpcContext;
import edu.ouc.rpc.model.RpcException;
import edu.ouc.rpc.model.RpcRequest;
import edu.ouc.rpc.model.RpcResponse;

public final class RpcConsumer implements InvocationHandler{

	private String host;

	private int port;

	private Class<?> interfaceClass;

	private int TIMEOUT;

	//钩子
	private ConsumerHook hook;
	
	private static int nThreads = Runtime.getRuntime().availableProcessors() * 2;

	private static ExecutorService handlerPool = Executors.newFixedThreadPool(nThreads);

	/**
	 * 存放当前线程正在执行的异步方法
	 */
	private static final ThreadLocal<Set<String>> asyncMethods = new ThreadLocal<Set<String>>(){
		@Override
		public Set<String> initialValue()
		{
			return new LinkedHashSet<String>();
		}
	};
	public RpcConsumer targetHostPort(String host, int port){
		this.host = host;
		this.port = port;
		return this;
	}
	public RpcConsumer interfaceClass(Class<?> interfaceClass) {
		this.interfaceClass = interfaceClass;
		return this;
	}
	public RpcConsumer timeout(int timeout){
		this.TIMEOUT = timeout;
		return this;
	}
	public RpcConsumer hook(ConsumerHook hook){
		this.hook = hook;
		return this;
	}
	public Object newProxy(){
		return Proxy.newProxyInstance(RpcConsumer.class.getClassLoader(), new Class<?>[]{this.interfaceClass}, this);
	}

	/**
	 * 异步方法调用
	 * 
	 * @param methodName
	 */
	public void asynCall(String methodName) {
		asynCall(methodName, null);
	}

	/**
	 * 异步方法，支持callback
	 *
	 * @param methodName
	 * @param callbackListener
	 */
	public <T extends ResponseCallbackListener> void asynCall(final String methodName, T callbackListener) {
		//记录异步方法调用
		asyncMethods.get().add(methodName);
		RpcRequest request = new RpcRequest(methodName,null,null,RpcContext.getAttributes());
		
		//构造并提交FutureTask异步任务
		Future<RpcResponse> f = null;
		try {
			f = doInvoke(request);
		} catch (Exception e) {}

		RpcResponse response;
		
		if(callbackListener != null){
			try {
				//阻塞
				response = (RpcResponse) f.get(TIMEOUT,TimeUnit.MILLISECONDS);
				if(response.isError()){
					//执行回调方法
					callbackListener.onException(new RpcException(response.getErrorMsg()));
				}else{
					callbackListener.onResponse(response.getResponseBody());
				}
			} catch(TimeoutException e){
				callbackListener.onTimeout();
			}catch (Exception e) {}
		}else{
			//client端将从ResponseFuture中获取结果
			ResponseFuture.setFuture(f);
		}
		hook.after(request);
	}

	public void cancelAsyn(String methodName) {
		asyncMethods.get().remove(methodName);
	}

	/**
	 * 拦截目标方法->序列化method对象->发起socket连接
	 */
	@Override
	public Object invoke(Object proxy, Method method,
			Object[] args) throws Throwable {
		//如果是异步方法，立即返回null
		if(asyncMethods.get().contains(method.getName())) return null;
		Object retVal = null;

		RpcRequest request = new RpcRequest(method.getName(), method.getParameterTypes(),args,RpcContext.getAttributes());
		RpcResponse rpcResp  = null;
		try{
			Future<RpcResponse> response = doInvoke(request);
			//获取异步结果
			rpcResp  = (RpcResponse)response.get(TIMEOUT,TimeUnit.MILLISECONDS);
		}catch(TimeoutException e){
			throw e;
		}catch(Exception e){}
		
		if(!rpcResp.isError()){
			retVal = rpcResp.getResponseBody();
		}else{
			throw new RpcException(rpcResp.getErrorMsg());
		}
		hook.after(request);
		return retVal;
	}
	private Future<RpcResponse> doInvoke(final RpcRequest request) throws IOException, ClassNotFoundException{
		//插入钩子
		hook.before(request);
		//构造并提交FutureTask异步任务
		Future<RpcResponse> retVal = (Future<RpcResponse>) handlerPool.submit(new Callable<RpcResponse>(){
			@Override
			public RpcResponse call() throws Exception {
				Object res = null;
				try{
					//创建连接,获取输入输出流
					Socket socket = new Socket(host,port);
					try{
						ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
						ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
						try{
							//发送
							out.writeObject(request);
							//接受server端的返回信息---阻塞
							res = in.readObject();
						}finally{
							out.close();
							in.close();
						}
					}finally{
						socket.close();
					}
				}catch(Exception e){
					throw e;
				}
				return (RpcResponse)res;
			}
		});
		return retVal;
	}
}

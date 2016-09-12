package edu.ouc.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.ouc.rpc.context.RpcContext;

/**
 * rpc服务类
 * 
 * @author wqx
 *
 */
public final class RpcBuilder {

	public static Object buildRpcClient(final Class<?> interfaces,final String host,final int port){
		if(interfaces == null){
			throw new IllegalArgumentException("interfaces can not be null");
		}

		return Proxy.newProxyInstance(RpcBuilder.class.getClassLoader(), new Class<?>[]{interfaces},
				new InvocationHandler(){

			//拦截目标方法->序列化method对象->发起socket连接
			public Object invoke(Object proxy, Method method,
					Object[] args) throws Throwable {

				//创建连接,获取输入输出流
				Socket socket = new Socket(host,port);
				Object retVal = null;
				try{

					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
					try{
						//构造请求参数对象
						
						RpcRequest request = new RpcRequest(method.getName(), method.getParameterTypes(),args,RpcContext.getAttributes());
						//发送
						out.writeObject(request);

						//接受server端的返回信息---阻塞
						Object response = in.readObject();

						if(response instanceof RpcResponse){
							RpcResponse rpcResp  = (RpcResponse)response;
							if(!rpcResp.isError()){
								retVal = rpcResp.getResponseBody();
							}else{
								throw new RpcException(rpcResp.getErrorMsg());
							}
						}
					}finally{
						out.close();
						in.close();
					}
				}finally{
					socket.close();
				}
				return retVal;
			}
		});
	}
	private static int nThreads = Runtime.getRuntime().availableProcessors() * 2;
	private static ExecutorService handlerPool = Executors.newFixedThreadPool(nThreads);

	public static void buildRpcServer(final Object service, final int port) throws IOException{
		if (service == null)  
			throw new IllegalArgumentException("service can not be null.");

		ServerSocket server = new ServerSocket(port);
		System.out.println("server started!!!");
		while(true){
			Socket socket = server.accept();//监听请求--阻塞

			//异步处理
			handlerPool.submit(new Handler(service,socket));
		}
	}
	static class Handler implements Runnable{

		private Object service;

		private Socket socket;

		public Handler(Object service,Socket socket){
			this.service = service;
			this.socket = socket;
		}
		public void run() {
			try{
				ObjectInputStream in = null;
				ObjectOutputStream out = null;
				RpcResponse response = new RpcResponse();
				try {
					in = new ObjectInputStream(socket.getInputStream());
					out = new ObjectOutputStream(socket.getOutputStream());

					Object req = in.readObject();
					if(req instanceof RpcRequest){
						RpcRequest rpcRequest = (RpcRequest)req;
						//关联客户端传来的上下文
						RpcContext.context.set(rpcRequest.getContext());
						Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
						Object retVal = method.invoke(service, rpcRequest.getArgs());
						response.setResponseBody(retVal);
						out.writeObject(response);
					}
				} catch (InvocationTargetException e) {
					response.setErrorMsg(e.getTargetException().getMessage());
					response.setResponseBody(e.getTargetException());
					out.writeObject(response);
				}finally{
					in.close();
					out.close();
				}
			}catch(Exception e){}
		}
	}
}

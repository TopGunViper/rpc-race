package edu.ouc.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.CollectionUtils;

import edu.ouc.rpc.context.RpcContext;
import edu.ouc.rpc.interceptor.DefaultMethodInvocation;
import edu.ouc.rpc.interceptor.Interceptor;
import edu.ouc.rpc.interceptor.InterceptorChainFactory;
import edu.ouc.rpc.interceptor.MethodInvocation;
import edu.ouc.rpc.model.RpcRequest;
import edu.ouc.rpc.model.RpcResponse;

public final class RpcProvider {


	public final static InterceptorChainFactory interceptorChain = new InterceptorChainFactory();

	private static int nThreads = Runtime.getRuntime().availableProcessors() * 2;
	private static ExecutorService handlerPool = Executors.newFixedThreadPool(nThreads);

	public static void publish(final Object service, final int port) throws IOException{
		if (service == null)
			throw new IllegalArgumentException("service can not be null.");

		ServerSocket server = new ServerSocket(port);
		System.out.println("server started!!!");
		while(true){
			Socket socket = server.accept();//监听请求--阻塞
			//异步处理
			handlerPool.submit(new Handler(service,socket,interceptorChain.getInterceptors()));
		}
	}
	static class Handler implements Runnable{

		private Object service;

		private Socket socket;

		List<Interceptor> interceptors;

		public Handler(Object service,Socket socket,List<Interceptor> interceptors){
			this.service = service;
			this.socket = socket;
			this.interceptors = interceptors;
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
						Object retVal = null;

						MethodInvocation methodInvocation;
						List<Interceptor> chain = interceptorChain.getInterceptors();
						if(!CollectionUtils.isEmpty(chain)){
							//执行拦截器链
							methodInvocation = new DefaultMethodInvocation(service,null,method,rpcRequest.getArgs(),chain);
							retVal = methodInvocation.executeNext();
						}else{
							retVal = method.invoke(service, rpcRequest.getArgs());
						}
						response.setResponseBody(retVal);
						out.writeObject(response);
					}
				} catch (Exception e) {
					response.setErrorMsg(e.getMessage());
					response.setResponseBody(e);
					out.writeObject(response);
				}finally{
					in.close();
					out.close();
				}
			}catch(Exception e){}
		}
	}
	public InterceptorChainFactory getInterceptorChain() {
		return interceptorChain;
	}
}

package edu.ouc.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.ouc.rpc.context.RpcContext;

public final class RpcProvider {
	
	
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

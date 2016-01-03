package com.alibaba.middleware.race.rpc.api.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.alibaba.middleware.race.rpc.api.RpcProvider;
import com.alibaba.middleware.race.rpc.model.RpcException;
import com.alibaba.middleware.race.rpc.service.RpcRequest;
import com.alibaba.middleware.race.rpc.service.RpcResponse;
import com.alibaba.middleware.race.rpc.util.SerializableUtil;

public class RpcProviderImpl extends RpcProvider {

	private final int DEFAULT_PORT = 8888;
	private final int nThreads = 10;

	private Class<?> className;
	private String interfaceName;
	private String version;
	private int timeout;
	private String type;

	private Object service;

	//服务端用于处理请求的线程池
	ExecutorService executor = Executors.newFixedThreadPool(nThreads);

	//多路复用器
	private Selector selector;

	private ServerSocketChannel ssc;

	private volatile boolean selectable;

	public void publish() {
		// TODO Auto-generated method stub
		if(!selectable){
			try {
				init();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private  void init() throws IOException{
		selector = Selector.open();
		ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ssc.socket().bind(new InetSocketAddress(DEFAULT_PORT),1024);
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		selectable = true;
		System.out.println("server init()");
		new AcceptorThread().start();
	}
	private void handle(SelectionKey key) throws IOException, ClassNotFoundException{
		if(key == null || !key.isValid()){
			return ;
		}
		System.out.println("server key.isAcceptable():" + key.isAcceptable() + ",key.isReadable()" + key.isReadable());
		if(key.isAcceptable()){
			ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
			SocketChannel channel = ssc.accept();
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_READ);
		}
		if(key.isReadable()){
			SocketChannel sc = (SocketChannel)key.channel();
			ByteBuffer readBuffer = ByteBuffer.allocate(1024);
			int readBytes = sc.read(readBuffer);
			if(readBytes > 0){
				readBuffer.flip();
				byte[] bytes = new byte[readBuffer.remaining()];
				readBuffer.get(bytes);
				RpcRequest request = (RpcRequest)SerializableUtil.deserializeObject(bytes);
				//doWrite(sc,new RpcResponse("wqx"));
				selectable = false;
				new ProcessTask(request,sc).start();
			}else if(readBytes == 0){
				
			}else{
				key.cancel();
				sc.close();
			}
		}
	}

	private class AcceptorThread extends Thread{

		public void run() {
			// TODO Auto-generated method stub
			while(selectable){
				try {
					int s = selector.select();
					if(s > 0){
						Set<SelectionKey> keys = selector.selectedKeys();
						Iterator<SelectionKey> it = keys.iterator();
						SelectionKey key = null;
						while(it.hasNext()){
							key = it.next();
							it.remove();
							//System.out.println("key:" + key.isAcceptable());
							try {
								handle(key);
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//handleInput(key);
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					selectable = false;
					e.printStackTrace();
				}
			}
			
			if(selector != null){
				try {
					selector.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	private RpcResponse process0(RpcRequest req){
		RpcResponse rsp = new RpcResponse(req.getRequestID());
		String methodName = req.getMethodName();
		Class<?>[] parameterTypes = req.getParameterTypes();
		Object[] parameters = req.getParameters();
		Class<?> interfaceType = req.getInterfaceType();
		long timeout = req.getRpcTimeoutInMillis();
		Method method;
		try {
			method = interfaceType.getDeclaredMethod(methodName, parameterTypes);
			Object result = method.invoke(service, parameters);
			rsp.setResult(result);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rsp;
	}
	/**
	 * IO处理线程
	 */
	private class ProcessTask  extends Thread {


		private RpcRequest req;
		private SocketChannel channel;


		public ProcessTask(RpcRequest req, SocketChannel channel) {
			this.req     = req;
			this.channel = channel;
		}
		@Override
		public void run() {
			RpcResponse rsp;
			try {
				Future<RpcResponse> future = executor.submit(new Callable<RpcResponse>(){
					public RpcResponse call() throws Exception {
						// TODO Auto-generated method stub
						return process0(req);
					}
				});
				// Wait response
				long timeout = req.getRpcTimeoutInMillis() > 0 ? req.getRpcTimeoutInMillis() : Integer.MAX_VALUE;
				System.out.println("timeOut:" + timeout);
				rsp = future.get(timeout, TimeUnit.MILLISECONDS);
				System.out.println("rsp.getResult():" + rsp.getResult());
			} catch (ExecutionException e) {
				rsp = new RpcResponse(req.getRequestID(), new RpcException(RpcException.SERVER_ERROR,"server error",e));
			} catch (TimeoutException e) {
				rsp = new RpcResponse(req.getRequestID(), new RpcException(RpcException.SERVER_TIMEOUT,"server timeout",e));
			} catch (Exception e) {
				rsp = new RpcResponse(req.getRequestID(), new RpcException(RpcException.UNKNOWN,"unknown error",e));
			}

			try {
				//写回客户端
				//channel
				doWrite(channel,rsp);
			} catch (Exception e) {

			}
		}
	}
	private void doWrite(SocketChannel channel , RpcResponse response) throws IOException{
		if(response != null){
			byte[] bytes = SerializableUtil.serializeObject(response);
			ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
			System.out.println("server bytes.length:" + bytes.length);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			channel.write(writeBuffer);
		}
	}
	public RpcProvider serviceInterface(Class<?> type) {
		this.className = type;
		this.interfaceName = type.getName();
		return this;
	}
	public RpcProvider impl(Object service) {
		// TODO Auto-generated method stub
		this.service = service;
		return this;
	}

	public RpcProvider version(String version) {
		// TODO Auto-generated method stub
		this.version = version;
		return this;
	}

	public RpcProvider timeout(int timeout) {
		// TODO Auto-generated method stub
		this.timeout = timeout;
		return this;
	}

	public RpcProvider serializeType(String type) {
		// TODO Auto-generated method stub
		this.type = type;
		return this;
	}
	public Class<?> getClassName() {
		return className;
	}

	public void setClassName(Class<?> className) {
		this.className = className;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getService() {
		return service;
	}

	public void setService(Object service) {
		this.service = service;
	}


}

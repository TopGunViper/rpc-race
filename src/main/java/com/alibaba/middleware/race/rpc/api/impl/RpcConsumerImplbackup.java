//package com.alibaba.middleware.race.rpc.api.impl;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Method;
//import java.lang.reflect.Proxy;
//import java.net.InetSocketAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.SocketAddress;
//import java.nio.ByteBuffer;
//import java.nio.channels.SelectionKey;
//import java.nio.channels.Selector;
//import java.nio.channels.ServerSocketChannel;
//import java.nio.channels.SocketChannel;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeoutException;
//
//import com.alibaba.middleware.race.rpc.aop.ConsumerHook;
//import com.alibaba.middleware.race.rpc.api.RpcConsumer;
//import com.alibaba.middleware.race.rpc.async.ResponseFuture;
//import com.alibaba.middleware.race.rpc.context.RpcContext;
//import com.alibaba.middleware.race.rpc.service.RpcRequest;
//import com.alibaba.middleware.race.rpc.service.RpcResponse;
//import com.alibaba.middleware.race.rpc.util.SerializableUtil;
//
//public class RpcConsumerImplbackup implements RpcConsumer  {
//
//	private final int DEFAULT_PORT = 8888;
//
//	private final String DEFAULT_HOST = "127.0.0.1";
//
//	private final String REMOTE_HOST = "114.215.130.4";
//
//	private Class<?> interfaceClass;
//	private String version;
//	private int timeout;
//
//	private Object listener;
//	private ConsumerHook hook;
//
//	private Selector selector;
//	private SocketChannel sc;
//	private volatile boolean stop;
//
//	public RpcConsumerImplbackup(){
//		try {
//			selector = Selector.open();
//			sc = SocketChannel.open();
//			sc.configureBlocking(false);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	private void doConnect() throws IOException{
//		boolean connected = sc.connect(new InetSocketAddress(DEFAULT_HOST,DEFAULT_PORT));
//		InetSocketAddress address = (InetSocketAddress) sc.getRemoteAddress();
//		System.out.println("connected:" + connected + ",host:" + address.getHostName() + ",port:" + address.getPort());
//		//如果连接成功，则注册到多路复用器selector上，发送请求信息，读应答
//		if(connected){
//			sc.register(selector, SelectionKey.OP_READ);
//		}else{
//			sc.register(selector, SelectionKey.OP_CONNECT);
//		}
//	}
//	public Object instance() {
//		// TODO Auto-generated method stub
//		if(interfaceClass == null)
//			throw new IllegalArgumentException("interface class = null");
//		if(!interfaceClass.isInterface())
//			throw new IllegalArgumentException("The " + interfaceClass.getClass().getName() + " must be a interface!");
//		try {
//			doConnect();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class<?>[]{interfaceClass}, new InvocationHandler(){
//
//			public Object invoke(Object proxy, Method method, Object[] arguments)
//					throws Throwable {
//				// TODO Auto-generated method stub
//				String methodName = method.getName();
//				Class<?>[] parameterTypes = method.getParameterTypes();
//				//创建请求并初始化
//				RpcRequest req = new RpcRequest();
//				req.setMethodName(methodName);
//				req.setParameterTypes(parameterTypes);
//				req.setParameters(arguments);
//				req.setInterfaceType(interfaceClass);
//				try {
//					RpcContext context = RpcContext.getRpcContext();
//					//req.setRpcTimeoutInMillis(rpcTimeoutInMillis(context));
//					req.setRequestID(context.getRpcId());
//					boolean async = context.isAsync(methodName);
//					return send(req, async);
//				} finally {
//					RpcContext.removeRpcContext();
//				}
//			}
//		});
//	}
//	private RpcResponse send(RpcRequest req , boolean async) throws IOException, InterruptedException, ExecutionException, TimeoutException{
//		/**
//		 *  RpcRequest -》 RpcEncoder --》 bytep[] - > server
//		 *  server - > byte[] - >  RpcDecoder -》  RpcResponse 
//		 */
//		
//		
//		while(!stop){
//			int s = selector.select();
//			System.out.println("stop:" + stop + ",selector.select():" + s);
//			if(s > 0){
//				Set<SelectionKey> keys = selector.selectedKeys();
//				Iterator<SelectionKey> it = keys.iterator();
//				SelectionKey key = null;
//				while(it.hasNext()){
//					key = it.next();
//					it.remove();
//					try {
//						return handle(key,req);
//					} catch (ClassNotFoundException e) {
//						// TODO Auto-generated catch block
//						if(key != null)
//							key.cancel();
//						e.printStackTrace();
//					}
//				}
//				//
//				//			System.out.println("methodName:" + req.getMethodName());
//				//			byte[] request = SerializableUtil.serializeObject(req);
//				//			ByteBuffer buf = ByteBuffer.allocate(1024);
//				//			buf.clear();
//				//			buf.put(request);
//				//			buf.flip();
//				//			while(buf.hasRemaining()){
//				//				sc.write(buf);
//				//			}
//				//			ResponseFuture<Object> future = null;
//				//			if(async){
//				//				future = new ResponseFuture<Object>();
//				//
//				//				return (RpcResponse)future.get(timeout, TimeUnit.SECONDS);
//				//			}else{
//				//
//				//			}
//				//			return (RpcResponse)future.getResponse();
//			}
//		}
//	}
//	private RpcResponse handle(SelectionKey key,RpcRequest req) throws IOException, ClassNotFoundException{
//		if(key.isValid()){
//			SocketChannel sc = (SocketChannel) key.channel();
//			System.out.println("client key.isConnectable():" + key.isConnectable() + ",key.isReadable()" + key.isReadable() + ",sc.finishConnect():" + sc.finishConnect());
//			//if(key.isConnectable()){
//			if(key.isReadable()){
//					ByteBuffer readBuffer = ByteBuffer.allocate(1024*10);
//					int readBytes = sc.read(readBuffer);
//					System.out.println("client readBytes:" + readBytes); 
//					if(readBytes > 0){
//						readBuffer.flip();
//						byte[] bytes = new byte[readBytes];
//						readBuffer.get(bytes);
//						RpcResponse response = (RpcResponse)SerializableUtil.deserializeObject(bytes);
//						System.out.println("response.getResult():" + response.getResult());
//						this.stop = true;
//						return response;
//					}else if(readBytes < 0){
//						key.cancel();
//						sc.close();
//					}
//			}else if(sc.finishConnect() ){
//				sc.register(selector, SelectionKey.OP_READ);
//				doWriter(sc,req);
//			}else{
//				System.out.println("连接失败。。。");
//				System.exit(1);
//			}
//			//}
//		}
//		return new RpcResponse("");
//	}
//	private void doWriter(SocketChannel sc,RpcRequest req) throws IOException{
//		byte[] request = SerializableUtil.serializeObject(req);
//		ByteBuffer buf = ByteBuffer.allocate(1024);
//		buf.put(request);
//		buf.flip();
//		sc.write(buf);
//		System.out.println("send to server success");
//	}
//
//	/**
//	 * newRpcClient()--> open() --> proxy invoke(localData) --> RpcInvoker.invoke(Request)
//	 * --> isAsync? timeout? set to request -->connector.send(RequestMsg)
//	 */
//	public void asynCall(String call) {
//		// TODO Auto-generated method stub
//		RpcContext ctx = RpcContext.getRpcContext();
//		RpcContext.addProp("async", call);
//	}
//
//	public void cancelAsyn(String call) {
//		// TODO Auto-generated method stub
//		RpcContext ctx = RpcContext.getRpcContext();
//		Map<String,Object> map = ctx.getProps();
//		for(String key : map.keySet()){
//			if(call.equals(key)){
//				map.remove(key);
//			}
//		}
//	}
//
//	public void asynCall(String call, Object listener) {
//		// TODO Auto-generated method stub
//
//	}
//
//	public RpcConsumer  version(String version) {
//		// TODO Auto-generated method stub
//		this.version = version;
//		return this;
//	}
//
//	public RpcConsumer  clientTimeout(int timeout) {
//		// TODO Auto-generated method stub
//		this.timeout = timeout;
//		return this;
//	}
//
//	public RpcConsumer  hook(ConsumerHook hook) {
//		// TODO Auto-generated method stub
//		this.hook = hook;
//		return this;
//	}
//
//
//
//	public Class<?> getInterfaceClass() {
//		return interfaceClass;
//	}
//	public void setInterfaceClass(Class<?> interfaceClass) {
//		this.interfaceClass = interfaceClass;
//	}
//	public String getVersion() {
//		return version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}
//
//	public int getTimeout() {
//		return timeout;
//	}
//
//	public void setTimeout(int timeout) {
//		this.timeout = timeout;
//	}
//
//	public ConsumerHook getHook() {
//		return hook;
//	}
//
//	public void setHook(ConsumerHook hook) {
//		this.hook = hook;
//	}
//	public RpcConsumer  interfaceClass(Class<?> interfaceClass) {
//		// TODO Auto-generated method stub
//		this.interfaceClass = interfaceClass;
//		return this;
//	}
//
//}

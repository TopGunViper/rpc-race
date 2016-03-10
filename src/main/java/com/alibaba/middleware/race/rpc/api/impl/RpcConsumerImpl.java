package com.alibaba.middleware.race.rpc.api.impl;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import com.alibaba.middleware.race.rpc.aop.ConsumerHook;
import com.alibaba.middleware.race.rpc.api.*;
import com.alibaba.middleware.race.rpc.async.ResponseCallbackListener;
import com.alibaba.middleware.race.rpc.async.ResponseFuture;
import com.alibaba.middleware.race.rpc.context.RpcContext;
import com.alibaba.middleware.race.rpc.model.RpcRequest;
import com.alibaba.middleware.race.rpc.model.RpcResponse;



public class RpcConsumerImpl extends RpcConsumer
{
	private static ThreadLocal<Set<String>> asynLocalSet = new ThreadLocal<Set<String>>(){
		@Override
		public Set<String> initialValue()
		{
			return new HashSet<String>();
		}
	};
	private static int PORT = 8888;
	public static ConsumerHook consumerHook;
	public static int clientTimeout;
	private Class<?> interfaceClazz;
	private static String host;
	private ChannelFuture f;
	//private ConsumerHandlerPool consumerHandlerPool;
	
    public RpcConsumerImpl() {
    	super();
    	host = System.getProperty("SIP", "127.0.0.1");
    	//consumerHandlerPool = new ConsumerHandlerPool(host, PORT);
    }
    
   

    /**
     * set the interface which this consumer want to use
     * actually,it will call a remote service to get the result of this interface's methods
     *
     * @param interfaceClass
     * @return
     */
    @Override
    public RpcConsumer interfaceClass(Class<?> interfaceClass) {
        this.interfaceClazz = interfaceClass;
        return this;
    }

    ;

    /**
     * set the version of the service
     *
     * @param version
     * @return
     */
    @Override
    public RpcConsumer version(String version) {
        //TODO
        return this;
    }

    /**
     * set the timeout of the service
     * consumer's time will take precedence of the provider's timeout
     *
     * @param clientTimeout
     * @return
     */
    @Override
    public RpcConsumer clientTimeout(int clientTimeout) {
        this.clientTimeout = clientTimeout;
        return this;
    }

    /**
     * register a consumer hook to this service
     * @param hook
     * @return
     */
    @Override
    public RpcConsumer hook(ConsumerHook hook) {
    	this.consumerHook = hook;
        return this;
    }

    /**
     * return an Object which can cast to the interface class
     *
     * @return
     */
    @Override
    public Object instance() {
        //TODO return an Proxy
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class[]{this.interfaceClazz},this);
    }

    /**
     * mark a async method,default future call
     *
     * @param methodName
     */
    @Override
    public void asynCall(String methodName) {
        asynCall(methodName, null);
    }

    /**
     * mark a async method with a callback listener
     *
     * @param methodName
     * @param callbackListener
     */
    @SuppressWarnings("unchecked")
	@Override
    public <T extends ResponseCallbackListener> void asynCall(final String methodName, 
    															T callbackListener) 
    {
    }
    
    @Override
    public void cancelAsyn(String methodName) {
        asynLocalSet.get().remove(methodName); //解除锁定
    }

    private boolean isAsyn(String methodName)
    {
    	return asynLocalSet.get().contains(methodName);
    }
    
    //调用方法的协议：方法名  参数类型  参数值  上下文
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
    {
    	return null;
    }
}


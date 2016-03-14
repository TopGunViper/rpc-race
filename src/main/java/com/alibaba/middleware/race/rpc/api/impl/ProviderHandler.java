package com.alibaba.middleware.race.rpc.api.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.alibaba.middleware.race.rpc.model.RpcRequest;
import com.alibaba.middleware.race.rpc.model.RpcResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProviderHandler extends ChannelInboundHandlerAdapter {
	private Object serviceInstance;
	
	public ProviderHandler(Object service){
		this.serviceInstance = service;
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// TODO Auto-generated method stub

		//handle request
		System.out.println("server reveive msg");
		ctx.writeAndFlush(handleRequest((RpcRequest)msg));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		cause.printStackTrace();
		ctx.close();
	}
	/**
	 * 获取请求对象req，通过反射调用服务器端本地方法得出结果并返回
	 */
	private RpcResponse handleRequest(RpcRequest req){
		RpcResponse resp = new RpcResponse();
		
		String methodName = req.getMethodName();
		Class<?>[] parameterTypes = req.getParameterTypes();
		Object[] parameters = req.getArgs();

		try {
			Method method = serviceInstance.getClass().getMethod(methodName, parameterTypes);
			Object result = method.invoke(serviceInstance, parameters);
			resp.setAppResponse(result);
		}catch(Throwable t){
			// TODO Auto-generated catch block
			resp.setErrorMsg("error");
			resp.setAppResponse(t);
		}
		return resp;
	}
}

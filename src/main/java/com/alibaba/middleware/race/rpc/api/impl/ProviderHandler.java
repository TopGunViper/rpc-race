package com.alibaba.middleware.race.rpc.api.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.alibaba.middleware.race.rpc.model.RpcRequest;
import com.alibaba.middleware.race.rpc.model.RpcResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProviderHandler extends ChannelInboundHandlerAdapter {

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

		Class<?> interfaceType = req.getInterfaceType();
		String methodName = req.getMethodName();
		Class<?>[] parameterTypes = req.getParameterTypes();
		Object[] parameters = req.getParameters();

		try {
			Method method = interfaceType.getMethod(methodName, parameterTypes);
			Object result = method.invoke(interfaceType, parameters);
			resp.setResult(result);
		}catch(Throwable t){
			// TODO Auto-generated catch block
			resp.setError(true);
			resp.setResult(t);
		}
		return resp;
	}
}

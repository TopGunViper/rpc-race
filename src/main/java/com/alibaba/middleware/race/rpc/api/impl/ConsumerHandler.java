package com.alibaba.middleware.race.rpc.api.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.alibaba.middleware.race.rpc.model.RpcRequest;
import com.alibaba.middleware.race.rpc.model.RpcResponse;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ConsumerHandler extends ChannelInboundHandlerAdapter {
	
	private Channel channel;
	
	private BlockingQueue<RpcResponse> queue = new LinkedBlockingQueue<RpcResponse>();
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		channel = ctx.channel();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// TODO Auto-generated method stub
		RpcResponse resp = (RpcResponse)msg;
		queue.offer(resp);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		cause.printStackTrace();
		ctx.close();
	}
	public RpcResponse sendRequest(RpcRequest req){
		channel.writeAndFlush(req);
		RpcResponse resp;
		while(true){
			try {
				resp = queue.take();
				break;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}
		}
		return resp;
	}
}

package com.alibaba.middleware.race.rpc.api.impl;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * RpcDecoder提供RPC解码，扩展Netty的ByteToMessageDecoder的decode抽象方法
 * @author wqx
 *
 */
public class RpcDecoder extends ByteToMessageDecoder {

	private Class<?> interfaceType;
	
	public RpcDecoder(Class<?> type){
		this.interfaceType = type;
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		
	}

}

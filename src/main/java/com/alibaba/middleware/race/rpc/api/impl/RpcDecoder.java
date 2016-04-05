package com.alibaba.middleware.race.rpc.api.impl;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * RpcDecoder提供RPC解码
 * 扩展Netty的LengthFieldBasedFrameDecoder的decode方法,解决TCP粘包问题
 * 
 * @author wqx
 *
 */
public class RpcDecoder extends LengthFieldBasedFrameDecoder {

	public RpcDecoder(int maxObjectSize){
		super(maxObjectSize, 0, 4, 0, 4);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf buf)throws Exception {
		// TODO Auto-generated method stub
		System.out.println("decode" + buf.array().length);
		ByteBuf res = (ByteBuf) super.decode(ctx, buf);
		byte[] out = new byte[res.readableBytes()];
		
		return SerializableUtil.deserializeObject(out);
	}

	@Override
	protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer,
			int index, int length) {
		// TODO Auto-generated method stub
		return super.extractFrame(ctx, buffer, index, length);
	}
	

}

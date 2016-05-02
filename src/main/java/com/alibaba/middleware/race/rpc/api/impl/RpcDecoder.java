package com.alibaba.middleware.race.rpc.api.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
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
		ByteBuf b = (ByteBuf) super.decode(ctx, buf);
        if (b == null) {
            return null;
        }
		byte[] out = new byte[b.readableBytes()];
		b.readBytes(out);
		return SerializableUtil.deserializeObject(out);
	}

	@Override
	protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer,
			int index, int length) {
		// TODO Auto-generated method stub
		return super.extractFrame(ctx, buffer, index, length);
	}
	

}

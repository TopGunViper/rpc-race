package com.alibaba.middleware.race.rpc.api.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 
 * 继承MessageToByteEncoder实现编码器
 * @author wqx
 *
 */
public class RpcEncoder extends MessageToByteEncoder<Object>{

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
			throws Exception {
		// TODO Auto-generated method stub
        byte[] body = SerializableUtil.FSTserialize(msg);
        int dataLength = body.length;
        out.writeInt(dataLength);
        out.writeBytes(body);
	}
	
}

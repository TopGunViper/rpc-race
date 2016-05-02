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

	private static final byte[] HOLDER = new byte[4];
	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
			throws Exception {
		// TODO Auto-generated method stub
		int index = out.writerIndex();
		out.writeBytes(HOLDER);
		out.writeBytes(SerializableUtil.serializeObject(msg));
		out.setInt(index, out.writerIndex() - index - 4);
	}

}

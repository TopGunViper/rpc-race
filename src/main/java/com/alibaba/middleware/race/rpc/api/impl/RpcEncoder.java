package com.alibaba.middleware.race.rpc.api.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcEncoder extends MessageToByteEncoder{

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
			throws Exception {
		// TODO Auto-generated method stub
        byte[] body = null;//SerializableUtil.FSTserialize(msg);
        int dataLength = body.length;
        out.writeInt(dataLength);
        out.writeBytes(body);
	}
	
}

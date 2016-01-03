package com.alibaba.middleware.race.rpc.api.impl;

import com.alibaba.middleware.race.rpc.api.RpcClient;
import com.alibaba.middleware.race.rpc.model.RpcException;
import com.alibaba.middleware.race.rpc.spi.RpcConnector;

public class RpcClientImpl implements RpcClient {
	
	private RpcConnector rpcConnector;
	
	public <T> T refer(Class<T> interfaceType) {
		// TODO Auto-generated method stub
		return null;
	}

	public void open() throws RpcException {
		// TODO Auto-generated method stub

	}

	public void close() {
		// TODO Auto-generated method stub

	}

}

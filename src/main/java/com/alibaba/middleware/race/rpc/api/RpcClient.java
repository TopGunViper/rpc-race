package com.alibaba.middleware.race.rpc.api;

import com.alibaba.middleware.race.rpc.model.RpcException;

/**
 * 
 * @author wqx
 * @version 1.0  23,Dec,2015
 *
 */
public interface RpcClient {
	
	/**
	 * Refer rpc api proxy instance
	 * @param interfaceType
	 * @return proxy instance
	 */
	<T> T refer(Class<T> interfaceType);
	
	/**
	 * Connect to rpc server
	 * @throws RpcException
	 */
	void open() throws RpcException;
	
	/**
	 * close connection
	 */
	void close();
}

package com.alibaba.middleware.race.rpc.service;

import com.alibaba.middleware.race.rpc.model.RpcException;

public class RpcResponse  implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5702920717986515134L;
	/**
	 * 请求ID
	 */
	private String requestID;
	/**
	 * 返回结果
	 */
	private Object result;
	/**
	 * 异常 
	 */
	private Throwable cause;
	
	public RpcResponse(String id,Object obj,RpcException cause){
		this.requestID = id;
		this.result = obj;
		this.cause = cause;
	}
	public RpcResponse(String id,RpcException rpcException){
		this.requestID = id;
		this.cause = rpcException;
	}
	public RpcResponse(String id){
		this.requestID = id;
	}
	public String getRequestID() {
		return requestID;
	}
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public Throwable getCause() {
		return cause;
	}
	public void setCause(Throwable cause) {
		this.cause = cause;
	}
	
	
}

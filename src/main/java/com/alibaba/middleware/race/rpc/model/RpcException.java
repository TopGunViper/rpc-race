package com.alibaba.middleware.race.rpc.model;


public final class RpcException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 568053428124563604L;
	
	//default exception code
	/**
	 * 网络异常 1
	 */
	public static final int NETWORK = 1;  
	/**
	 * 服务端超时异常
	 */
	public static final int SERVER_TIMEOUT = 2;
	/**
	 * 服务端错误
	 */
	public static final int SERVER_ERROR = 3;
	/**
	 * 服务端线程池过载
	 */
	public static final int SERVER_OVERLOAD = 4;
	/**
	 * 未知异常
	 */
	public static final int UNKNOWN = 0;
	
	private int code;
	
	
	
	public RpcException(){}

	public RpcException(int code){
		super("");
		this.code = code;
	}
	public RpcException(int code,String msg){
		super(msg);
		this.code = code;
	}
	public RpcException(int code,String msg,Throwable cause){
		super(msg,cause);
		this.code = code;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
}

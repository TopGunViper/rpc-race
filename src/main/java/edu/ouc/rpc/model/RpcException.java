package edu.ouc.rpc.model;

/**
 * 自定义异常
 * 
 * @author wqx
 *
 */
public class RpcException extends RuntimeException {

	private static final long serialVersionUID = -2157872157006208360L;
	
	public RpcException(String msg){
		super(msg);
	}
}

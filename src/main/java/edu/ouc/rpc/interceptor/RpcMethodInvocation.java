package edu.ouc.rpc.interceptor;

/**
 * RpcMethodInvocation
 * 
 * @author wqx
 *
 */
public interface RpcMethodInvocation extends MethodInvocation {
	
	/**
	 * 是否是rpc调用
	 * 
	 * @return
	 */
	public boolean isRpcInvocation();
	
	
}

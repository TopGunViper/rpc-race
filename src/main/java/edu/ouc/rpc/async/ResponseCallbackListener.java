package edu.ouc.rpc.async;

/**
 * 监听器
 * 
 * @author wqx
 *
 */
public interface ResponseCallbackListener {
	
	public void onResponse(Object response);
    
    public void onTimeout();
    
    public void onException(Exception e);
}

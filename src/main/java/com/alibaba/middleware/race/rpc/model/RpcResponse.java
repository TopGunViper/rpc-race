package com.alibaba.middleware.race.rpc.model;

import java.io.Serializable;

/**
 * Created by huangsheng.hs on 2015/3/27.
 */
public class RpcResponse implements Serializable{
    static private final long serialVersionUID = -4364536436151723421L;
    
    private Object appResponse;
    private String errorMsg;    
	

	public String getErrorMsg() {
		return errorMsg;
	}


	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


	public void setAppResponse(Object appResponse) {
		this.appResponse = appResponse;
	}

	
    public Object getAppResponse() {
        return appResponse;
    }


    public boolean isError(){
        return errorMsg == null ? false:true;
    }
}

package edu.ouc.rpc;

import java.io.Serializable;

/**
 * 响应对象
 * 
 * @author wqx
 *
 */
public class RpcResponse implements Serializable{
	
    static private final long serialVersionUID = -4364536436151723421L;
    
    //响应实体
    private Object responseBody;
    
    //错误信息
    private String errorMsg;
	
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

    public Object getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(Object responseBody) {
		this.responseBody = responseBody;
	}


	public boolean isError(){
        return errorMsg == null ? false:true;
    }
}

package com.alibaba.middleware.race.rpc.model;


public class RpcResponse implements java.io.Serializable{
    static private final long serialVersionUID = -4364536436151723421L;

    private String errorMsg;

    private Object appResponse;

    public Object getAppResponse() {
        return appResponse;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public boolean isError(){
        return errorMsg == null ? false:true;
    }
}

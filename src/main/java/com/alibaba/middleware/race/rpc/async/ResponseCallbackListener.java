package com.alibaba.middleware.race.rpc.async;

public interface ResponseCallbackListener {
	
	void onResponse(Object response);
	void onTimeout();
	void onException(Exception e);
	
}

package com.alibaba.middleware.race.rpc.model;

import java.io.Serializable;
import java.util.Map;


/**
 * Created by huangsheng.hs on 2015/5/7.
 */
public class RpcRequest implements Serializable
{
	
	private String methodName;
	private Class<?>[] parameterTypes;
	private Object[] args;
	private Map<String,Object> contextMap;
	
	public RpcRequest(String methodName,Class<?>[] parameterTypes,Object[] args,
						Map<String,Object> contextMap)
	{
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.args = args;
		this.contextMap = contextMap;
	}
	
	

	public Map<String, Object> getContextMap() {
		return contextMap;
	}



	public void setContextMap(Map<String, Object> contextMap) {
		this.contextMap = contextMap;
	}



	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	
	
}

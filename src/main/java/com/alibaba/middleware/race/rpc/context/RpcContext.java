package com.alibaba.middleware.race.rpc.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;

public class RpcContext {
	
	private static final ThreadLocal<RpcContext> rpcContext = new ThreadLocal<RpcContext>(){
		@Override
		protected synchronized RpcContext initialValue() {
			// TODO Auto-generated method stub
			return new RpcContext();
		}
	};
	
	private String rpcId;
	private Future<?> future;
	
	public RpcContext(){
	}
	
    public static Map<String,Object> props = new HashMap<String, Object>();

    public static void addProp(String key ,Object value){
        props.put(key,value);
    }

    public static Object getProp(String key){
        return props.get(key);
    }

    public static Map<String,Object> getProps(){
       return Collections.unmodifiableMap(props);
    }
    
	//url
	private String url;
	
	private String methodName;
	
	private Class<?>[] parameterTypes;
	
	private Object[] arguments;
	
	private Map<String,Object> attachments = new HashMap<String,Object>();

	
	public String getRpcId() {
		return rpcId;
	}

	public void setRpcId(String rpcId) {
		this.rpcId = rpcId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}


	public Map<String, Object> getAttachments() {
		return attachments;
	}

	public void setAttachments(Map<String, Object> attachments) {
		this.attachments = attachments;
	}

	public static RpcContext getRpcContext(){
		return rpcContext.get();
	}
	public static void removeRpcContext(){
		rpcContext.remove();
	}
	public  boolean isAsync(String methodName) {
		return props.containsKey(methodName);
	}

	public Future<?> getFuture() {
		return future;
	}

	public void setFuture(Future<?> future) {
		this.future = future;
	}
	
}

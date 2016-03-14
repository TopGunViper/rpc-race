package com.alibaba.middleware.race.rpc.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;
/**
 * ThreadLocal，为每个线程提供一个独立的变量副本，隔离多个线程对数据的访问冲突。
 * 
 * @author wqx
 *
 */
public class RpcContext {
	
	public static final ThreadLocal<Map<String,Object>> LOCAL  = new ThreadLocal<Map<String,Object>>(){
		@Override
		protected synchronized Map<String,Object> initialValue() {
			// TODO Auto-generated method stub
			return new HashMap<String,Object>();
		}
	};
	
	private String rpcId;
	private Future<?> future;
	
	public RpcContext(){
	}
	
    public  static void addProp(String key ,Object value){
    	LOCAL.get().put(key,value);
    }

    public static  Object getProp(String key){
        return LOCAL.get().get(key);
    }

    public static Map<String,Object> getProps(){
       return Collections.unmodifiableMap(LOCAL.get());
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
	public  boolean isAsync(String methodName) {
		return LOCAL.get().containsKey(methodName);
	}

	public Future<?> getFuture() {
		return future;
	}

	public void setFuture(Future<?> future) {
		this.future = future;
	}
	
}

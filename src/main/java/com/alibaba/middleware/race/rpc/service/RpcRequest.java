package com.alibaba.middleware.race.rpc.service;

import java.net.InetSocketAddress;
import java.util.Map;

public class RpcRequest implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -99874314780126808L;
	/**
	 * 
	 */
	
	private String requestID;
	private Class<?>  interfaceType;
	private String methodName;
	private Class<?> [] parameterTypes;
	private Object[] parameters;
	
	private Map<String, String> attachments;
	
	private Exception           exception;
	
	private InetSocketAddress serverAddress;
	private InetSocketAddress clientAddress;
	private Integer rpcTimeoutInMillis = Integer.MAX_VALUE;
	
	public RpcRequest(){}
	
	public RpcRequest(Class<?> type,String method,Class<?>[] parameterTypes,Object[] parameters){
		this.interfaceType = type;
		this.methodName = method;
		this.parameterTypes = parameterTypes;
		this.parameters = parameters;
	}
	
	public Class<?> getInterfaceType() {
		return interfaceType;
	}
	public void setInterfaceType(Class<?> interfaceType) {
		this.interfaceType = interfaceType;
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
	public Object[] getParameters() {
		return parameters;
	}
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}
	public Map<String, String> getAttachments() {
		return attachments;
	}
	public void setAttachments(Map<String, String> attachments) {
		this.attachments = attachments;
	}
	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
	public InetSocketAddress getServerAddress() {
		return serverAddress;
	}
	public void setServerAddress(InetSocketAddress serverAddress) {
		this.serverAddress = serverAddress;
	}
	public InetSocketAddress getClientAddress() {
		return clientAddress;
	}
	public void setClientAddress(InetSocketAddress clientAddress) {
		this.clientAddress = clientAddress;
	}
	public int getRpcTimeoutInMillis() {
		return rpcTimeoutInMillis;
	}
	public void setRpcTimeoutInMillis(int rpcTimeoutInMillis) {
		this.rpcTimeoutInMillis = rpcTimeoutInMillis;
	}

	public String getRequestID() {
		return requestID;
	}
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	
	
}

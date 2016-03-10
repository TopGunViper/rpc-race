package com.alibaba.middleware.race.rpc.model;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Map;

public class RpcMessage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6494200630149384241L;
	
	public static final short MAGIC = (short)  0xabcd;
	public static final short HEAD_SIZE = (short) 0x0010;
	
	/**
	 * head Info
	 */
	private short magic = MAGIC;
	private byte version = (byte) 0;
	private long serialId = (long)0;
	private short headSize = HEAD_SIZE;
	private short bodySize = (short) 0;
	
	
	/**
	 * body Info
	 */
	private Class<?>  rpcInterface;
	private String methodName;
	private Class<?> [] parameterTypes;
	private Object[] parameters;
	private Map<String, String> attachments;
	private Object      result;
	private Exception           exception;
	private InetSocketAddress serverAddress;
	private InetSocketAddress clientAddress;
	private int   rpcTimeoutInMillis = Integer.MAX_VALUE;

	
	
	
	
	public short getMagic() {
		return magic;
	}
	public void setMagic(short magic) {
		this.magic = magic;
	}
	public byte getVersion() {
		return version;
	}
	public void setVersion(byte version) {
		this.version = version;
	}
	public long getSerialId() {
		return serialId;
	}
	public void setSerialId(long serialId) {
		this.serialId = serialId;
	}
	public short getHeadSize() {
		return headSize;
	}
	public void setHeadSize(short headSize) {
		this.headSize = headSize;
	}
	public short getBodySize() {
		return bodySize;
	}
	public void setBodySize(short bodySize) {
		this.bodySize = bodySize;
	}
	public Class<?> getRpcInterface() {
		return rpcInterface;
	}
	public void setRpcInterface(Class<?> rpcInterface) {
		this.rpcInterface = rpcInterface;
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
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
}

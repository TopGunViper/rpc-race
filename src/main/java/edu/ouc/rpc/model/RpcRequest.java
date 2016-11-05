package edu.ouc.rpc.model;

import java.io.Serializable;
import java.util.Map;


/**
 * 封装请求参数
 * 
 * @author wqx
 *
 */
public class RpcRequest implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7102839100899303105L;
	
	//接口名
	private String interfaceName;
	
	//方法名
	private String methodName;
	
	//参数类型
	private Class<?>[] parameterTypes;
	
	//参数列表
	private Object[] args;
	
	private Map<String,Object> context;
	
	public RpcRequest(String interfaceName, String methodName,Class<?>[] parameterTypes,Object[] args, Map<String,Object> context)
	{
		this.interfaceName = interfaceName;
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.args = args;
		this.context = context;
	}
	
	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
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

	public Map<String, Object> getContext() {
		return context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}
	
}

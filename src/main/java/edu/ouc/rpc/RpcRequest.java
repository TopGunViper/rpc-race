package edu.ouc.rpc;

import java.io.Serializable;


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

	//方法名
	private String methodName;
	
	//参数类型
	private Class<?>[] parameterTypes;
	
	//参数列表
	private Object[] args;
	
	public RpcRequest(String methodName,Class<?>[] parameterTypes,Object[] args)
	{
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.args = args;
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

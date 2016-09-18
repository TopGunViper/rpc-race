package edu.ouc.rpc.interceptor;

public interface Interceptor {
	
	Object intercept(MethodInvocation invocation) throws Exception;
	
}

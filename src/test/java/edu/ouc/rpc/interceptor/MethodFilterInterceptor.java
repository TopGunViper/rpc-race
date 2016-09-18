package edu.ouc.rpc.interceptor;

import java.util.HashSet;
import java.util.Set;

import edu.ouc.rpc.model.RpcException;

public class MethodFilterInterceptor implements Interceptor {
	
	private Set<String> exclusions = new HashSet<>();
	
	public MethodFilterInterceptor(Set<String> exclusions){
		this.exclusions = exclusions;
	}
	@Override
	public Object intercept(MethodInvocation invocation) throws Exception {
		String methodName = invocation.getMethod().getName();
		if(exclusions.contains(methodName)){
			throw new RpcException("method " + methodName + " is not allowed!");
		}
		return invocation.executeNext();
	}
	
}

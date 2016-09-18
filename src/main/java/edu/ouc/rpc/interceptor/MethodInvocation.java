package edu.ouc.rpc.interceptor;

import java.lang.reflect.Method;

/**
 * 抽象出方法的执行
 * 
 * @author wqx
 *
 */
public interface MethodInvocation {
	
	/**
	 * 获取方法
	 * 
	 * @return
	 */
	public Method getMethod();
	
	/**
	 * 
	 * @return
	 */
	public Object[] getParameters();
	
	/**
	 * invoke next Interceptor
	 * 
	 * @return
	 * @throws Exception
	 */
	Object executeNext() throws Exception;
	
}

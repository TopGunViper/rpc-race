package edu.ouc.rpc.interceptor;

import java.util.HashSet;
import java.util.Set;

import edu.ouc.rpc.context.RpcContext;

/**
 * TimeInterceptor：记录方法执行时间
 * 
 * @author wqx
 *
 */
public class TimeInterceptor implements Interceptor {

	@Override
	public Object intercept(MethodInvocation invocation) throws Exception {
		long start = System.currentTimeMillis();
		Object retVal = null;
		try{
			retVal = invocation.executeNext();
		}finally{
			long end = System.currentTimeMillis();
			System.out.println("execute time :" + (end-start) + "ms.");
			RpcContext.addAttribute("time", (end-start));
		}
		return retVal;
	}
}

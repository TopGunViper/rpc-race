package edu.ouc.rpc.interceptor;

import java.lang.reflect.Method;
import java.util.List;

import edu.ouc.rpc.RpcConsumer;
import edu.ouc.rpc.context.RpcContext;
import edu.ouc.rpc.model.RpcRequest;

/**
 * MethodInvocation��Ĭ��ʵ��
 * 
 * @author wqx
 *
 */
public class DefaultMethodInvocation implements RpcMethodInvocation {

	private Object target;
	private Object proxy;
	private Method method;
	private Object[] parameters;
	
	private boolean isRpcInvocation;
	
	//拦截器
	private List<Interceptor> interceptors;
	
	//当前Interceptor索引值，范围：0-interceptors.size()-1
	private int currentIndex = -1;
	
	public DefaultMethodInvocation(Object target,Object proxy,Method method,Object[] parameters, List<Interceptor> interceptors){
		this.target = target;
		this.proxy = proxy;
		this.method = method;
		this.parameters = parameters;
		this.interceptors = interceptors;
	}
	
	@Override
	public Object executeNext() throws Exception {
		if(this.currentIndex == this.interceptors.size() - 1){
			if(this.isRpcInvocation){
				RpcRequest request = new RpcRequest(target.getClass().getName(), method.getName(),method.getParameterTypes(),parameters
						,RpcContext.getAttributes());
				return ((RpcConsumer)target).sendRequest(request);
			}else{
				method.setAccessible(true);
				return method.invoke(target, parameters);				
			}
		}
		Object interceptor = this.interceptors.get(++this.currentIndex);
		return ((Interceptor)interceptor).intercept(this);
	}
	
	//getter setter
	public Object getTarget() {
		return target;
	}
	public Object getProxy() {
		return proxy;
	}
	@Override
	public Method getMethod() {
		return method;
	}
	@Override
	public Object[] getParameters() {
		return parameters;
	}
	public List<?> getInterceptors() {
		return interceptors;
	}
	public int getCurrentIndex() {
		return currentIndex;
	}
	public void setTarget(Object target) {
		this.target = target;
	}
	public void setProxy(Object proxy) {
		this.proxy = proxy;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}
	public void setInterceptors(List<Interceptor> interceptors) {
		this.interceptors = interceptors;
	}
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	@Override
	public boolean isRpcInvocation() {
		// TODO Auto-generated method stub
		return isRpcInvocation;
	}

}

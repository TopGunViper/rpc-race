package edu.ouc.rpc.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * rpc上下文
 * 
 * @author wqx
 *
 */
public class RpcContext {
	
	public static ThreadLocal<Map<String,Object>> context = new ThreadLocal<Map<String,Object> >(){
    	@Override
        protected Map<String,Object> initialValue() {
            Map<String,Object> m = new HashMap<String,Object>();
            return m;
        }
	};
	
	public static void addAttribute(String key, Object value){
		context.get().put(key,value);
	}
	
	public static Object getAttribute(String key){
		return context.get().get(key);
	}
	
	public static Map<String,Object> getAttributes(){
		return Collections.unmodifiableMap(context.get());
	}
}

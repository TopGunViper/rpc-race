package edu.ouc.rpc.interceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.collections.CollectionUtils;

public class InterceptorChainFactory {
	
	private List<Entry> interceptors;
	
	private Set<String> registeredNames = Collections.newSetFromMap(
			new ConcurrentHashMap<String,Boolean>());
	
	public InterceptorChainFactory(){
		interceptors = new CopyOnWriteArrayList<Entry>();
	}
	
	public void addLast(String interceptorName, Interceptor interceptor){
		register(interceptors.size(),new Entry(interceptorName,interceptor));
	}
	public void addFirst(String interceptorName, Interceptor interceptor){
		register(0,new Entry(interceptorName,interceptor));
	}
	
	private void register(int index, Entry entry){
        if (registeredNames.contains(entry.name)) {
            throw new IllegalArgumentException("Other interceptor is using the same name: " + entry.name);
        }
        interceptors.add(index, entry);
	}
	
	@SuppressWarnings("unchecked")
	public List<Interceptor> getInterceptors() {
		if(!CollectionUtils.isEmpty(this.interceptors)){
			List<Interceptor> list = new ArrayList<>(this.interceptors.size());
			for(Entry entry: this.interceptors){
				list.add(entry.interceptor);
			}
			return Collections.unmodifiableList(list);
		}else{
			return (List<Interceptor>) CollectionUtils.EMPTY_COLLECTION;
		}
	}
	static class Entry{
		String name;
		Interceptor interceptor;

		public Entry(String name, Interceptor interceptor) {
			super();
			this.name = name;
			this.interceptor = interceptor;
		}
	}
}

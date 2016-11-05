package edu.ouc.rpc.interceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.collections.CollectionUtils;

public class InterceptorChain {
	
	private List<Entry> interceptors;
	
	private Set<String> registeredNames = new HashSet<String>();
	
	public InterceptorChain(){
		interceptors = new CopyOnWriteArrayList<Entry>();
	}
	
	public void addLast(String interceptorName, Interceptor interceptor){
		register(interceptors.size(),new Entry(interceptorName,interceptor));
	}
	public void addFirst(String interceptorName, Interceptor interceptor){
		register(0,new Entry(interceptorName,interceptor));
	}
	
	private synchronized void register(int index, Entry entry){
        if (registeredNames.contains(entry.name)) {
            throw new IllegalArgumentException("Other interceptor is using the same name: " + entry.name);
        }
        interceptors.add(index, entry);
        registeredNames.add(entry.name);
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

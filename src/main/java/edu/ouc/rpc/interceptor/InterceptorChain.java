package edu.ouc.rpc.interceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

public class InterceptorChain {

	private List<Entry> interceptors;

	private Set<String> registeredNames;

	public InterceptorChain(){
		interceptors = new ArrayList<Entry>();
		registeredNames = new HashSet<String>();
	}

	public void addLast(String name, Interceptor interceptor){
		synchronized(this){
			checkDuplicateName(name);
			Entry entry = new Entry(name,interceptor);
			register(interceptors.size(), entry);
		}
	}
	public void addFirst(String name, Interceptor interceptor){
		synchronized(this){
			checkDuplicateName(name);
			Entry entry = new Entry(name,interceptor);
			register(0, entry);
		}
	}

	public void addBefore(String baseName, String name, Interceptor interceptor){
		synchronized(this){
			checkDuplicateName(name);
			int index = getInterceptorIndex(baseName);
			if(index == -1)
				throw new NoSuchElementException(baseName);
			Entry entry = new Entry(name,interceptor);
			register(index, entry);
		}
	}
	
	public void addAfter(String baseName, String name, Interceptor interceptor){
		synchronized(this){
			checkDuplicateName(name);
			int index = getInterceptorIndex(baseName);
			if(index == -1)
				throw new NoSuchElementException(baseName);
			Entry entry = new Entry(name,interceptor);
			register(index+1, entry);
		}
	}
	
    private int getInterceptorIndex(String name) {
    	List<Entry> interceptors = this.interceptors;
    	for(int i = 0; i < interceptors.size(); i++){
    		if(interceptors.get(i).name.equals(name)){
    			return i;
    		}
    	}
        return -1;
    }
	private void register(int index, Entry entry){
		interceptors.add(index, entry);
		registeredNames.add(entry.name);
	}
	private void checkDuplicateName(String name) {
		if (registeredNames.contains(name)) {
			throw new IllegalArgumentException("Duplicate interceptor name: " + name);
		}
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

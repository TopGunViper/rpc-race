package edu.ouc.rpc.demo.service;

import java.util.HashMap;
import java.util.Map;

import edu.ouc.rpc.context.RpcContext;
import edu.ouc.rpc.model.RpcException;

/**
 * 测试业务接口实现类
 * 
 * @author wqx
 *
 */
public class UserServiceImpl implements UserService {

	public String test() {
		return "hello client, this is rpc server.";
	}
	
	public User queryUserById(int userId) {
		User parent = new User(100,"小明爸爸");
		User child = new User(101,"小明同学");
		parent.addChild(child);
		return parent;
	}
	
	public Object exceptionTest() throws RpcException {
		throw new RpcException("exception occur in server！！！");
	}

	@Override
	public Map<String,Object> rpcContextTest() {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("server","hahaha");
        if(RpcContext.getAttributes() != null )
        	map.putAll(RpcContext.getAttributes());
        return map;
	}
	
	@Override
	public boolean timeoutTest() {
		try {
			//模拟长时间执行
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {}
		return true;
	}

	@Override
	public Map<String, Object> getMap() {
        Map<String,Object> newMap = new HashMap<String,Object>();
        newMap.put("name","getMap");
        newMap.putAll(RpcContext.getAttributes());
        return newMap;
	}

}

package com.alibaba.middleware.race.rpc.demo.service;

import com.alibaba.middleware.race.rpc.context.RpcContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangsheng.hs on 2015/3/26.
 */
public class RaceTestServiceImpl implements RaceTestService{
	
    public Map<String, Object> getMap() {
        Map<String,Object> newMap = new HashMap<String,Object>();
        newMap.put("race","rpc");
        if(RpcContext.getProps() != null );
        newMap.putAll(RpcContext.getProps());
        return newMap;
    }

    public String getString() {
        return "this is a rpc framework";
    }

    public RaceDO getDO() {
        return new RaceDO();
    }

    public boolean longTimeMethod() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    public Integer throwException() throws RaceException{
        throw new RaceException("just a exception");
    }

	public void testMethod() {
		// TODO Auto-generated method stub
		System.out.println("testMethod!");
	}
}

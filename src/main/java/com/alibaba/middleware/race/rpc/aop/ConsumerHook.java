package com.alibaba.middleware.race.rpc.aop;

import com.alibaba.middleware.race.rpc.service.RpcRequest;

public interface ConsumerHook {
    public void before(RpcRequest request);
    public void after(RpcRequest request);
}

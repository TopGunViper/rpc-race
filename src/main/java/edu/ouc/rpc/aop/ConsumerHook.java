package edu.ouc.rpc.aop;

import edu.ouc.rpc.model.RpcRequest;

/**
 * @author wqx
 */
public interface ConsumerHook {
    public void before(RpcRequest request);
    public void after(RpcRequest request);
}

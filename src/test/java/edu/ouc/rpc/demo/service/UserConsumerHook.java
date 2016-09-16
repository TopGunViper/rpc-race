package edu.ouc.rpc.demo.service;

import edu.ouc.rpc.aop.ConsumerHook;
import edu.ouc.rpc.context.RpcContext;
import edu.ouc.rpc.model.RpcRequest;

public class UserConsumerHook implements ConsumerHook{
    @Override
    public void before(RpcRequest request) {
        RpcContext.addAttribute("hook key","this is pass by hook");
    }

    @Override
    public void after(RpcRequest request) {
        System.out.println("I have finished Rpc calling.");
    }
}

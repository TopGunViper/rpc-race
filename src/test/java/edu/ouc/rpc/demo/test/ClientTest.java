package edu.ouc.rpc.demo.test;


import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import edu.ouc.rpc.RpcConsumer;
import edu.ouc.rpc.async.ResponseFuture;
import edu.ouc.rpc.context.RpcContext;
import edu.ouc.rpc.demo.service.User;
import edu.ouc.rpc.demo.service.UserService;

public class ClientTest {

	
	private static String host = "127.0.0.1";
	private static int port = 8888;
	
	private static UserService userService;
	
	private static int TIMEOUT = 3000;
	
	private static RpcConsumer consumer;
	static {
		consumer = new RpcConsumer();
		userService = (UserService)consumer.targetHostPort(host, port)
							.interfaceClass(UserService.class)
							.timeout(TIMEOUT)
							.newProxy();
	}

	/**
	 * 基本调用链路测试
	 */
	@Ignore
	@Test
	public void test() {
		System.out.println(userService.test());
		Assert.assertEquals(userService.test(), "hello client, this is rpc server.");
	}
	/**
	 * 测试异常情况
	 */
	@Ignore
	@Test
	public void testException(){
		Object msg = null;
		try{
			msg = userService.exceptionTest();
		}catch(Exception e){
			Assert.assertEquals(e.getMessage(),"exception occur in server！！！");
		}
	}
	/**
	 * 测试自定义业务类
	 */
	@Ignore
	@Test
	public void testQueryUser() {
		Object msg = null;
		try{
			msg = userService.queryUserById(0);
			Assert.assertEquals(((User)msg).getName(),"小明爸爸");
			Assert.assertEquals(((User)msg).getChilds().get(0).getName(),"小明同学");
		}catch(Exception e){
		}
	}
	@Ignore
	@Test
	public void testRpcContext(){
        RpcContext.addAttribute("client","huhuhu");
        Map<String, Object> res = userService.rpcContextTest();
        Assert.assertEquals(res.get("server"),"hahaha");
        Assert.assertEquals(res.get("client"),"huhuhu");
	}
	@Test
	public void testAsyncCall(){
        consumer.asynCall("test");
        //立即返回
        String nullValue = userService.test();
        System.out.println(nullValue);
        Assert.assertEquals(null, nullValue);
        try {
            String result = (String) ResponseFuture.getResponse(TIMEOUT);
            Assert.assertEquals("hello client, this is rpc server.", result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            consumer.cancelAsyn("test");
        }
	}
}

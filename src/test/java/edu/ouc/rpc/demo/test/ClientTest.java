package edu.ouc.rpc.demo.test;


import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import edu.ouc.rpc.RpcConsumer;
import edu.ouc.rpc.async.ResponseFuture;
import edu.ouc.rpc.context.RpcContext;
import edu.ouc.rpc.demo.service.User;
import edu.ouc.rpc.demo.service.UserConsumerHook;
import edu.ouc.rpc.demo.service.UserService;
import edu.ouc.rpc.demo.service.UserServiceListener;
import edu.ouc.rpc.interceptor.TimeInterceptor;

public class ClientTest {


	private static String host = "127.0.0.1";
	private static int port = 8888;

	private static UserService userService;

	private static int TIMEOUT = 3000;

	private static RpcConsumer consumer;
	static {
		consumer = new RpcConsumer();

		consumer.getInterceptorChain().addLast("time", new TimeInterceptor());

		userService = (UserService)consumer.targetHostPort(host, port)
				.interfaceClass(UserService.class)
				.timeout(TIMEOUT)
				//.hook(new UserConsumerHook())
				.newProxy();

	}

	/**
	 * 基本调用链路测试
	 */
	@Ignore
	@Test
	public void test() {
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
	@Ignore
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
	@Ignore
	@Test
	public void testCallback() {
		UserServiceListener listener = new UserServiceListener();
		consumer.asynCall("test", listener);
		String nullStr = userService.test();
		Assert.assertEquals(null, nullStr);
		try {
			String str = (String)listener.getResponse();
			Assert.assertEquals("hello client, this is rpc server.", str);
		} catch (InterruptedException e) {
		}
	}
	@Ignore
	@Test
	public void timeoutTest(){
		long beginTime = System.currentTimeMillis();
		try {
			boolean result = userService.timeoutTest(); 
		} catch (Exception e) {
			long period = System.currentTimeMillis() - beginTime;
			System.out.println("period:" + period);
			Assert.assertTrue(period < 3100);
		}
	}
	@Ignore
	@Test
	public void testConsumerHook() {
		Map<String, Object> resultMap = userService.getMap();
		Assert.assertTrue(resultMap.containsKey("hook key"));
		Assert.assertTrue(resultMap.containsValue("this is pass by hook"));
	}

	@Test
	public void testInterceptorChain(){
		try{
			Map<String, Object> resultMap = userService.getMap();
		}catch(Exception e){
			System.out.println(e);
			Assert.assertEquals("method getMap is not allowed!", e.getMessage());
		}
	}

}

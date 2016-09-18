package edu.ouc.rpc.demo.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import edu.ouc.rpc.RpcProvider;
import edu.ouc.rpc.demo.service.UserService;
import edu.ouc.rpc.demo.service.UserServiceImpl;
import edu.ouc.rpc.interceptor.MethodFilterInterceptor;

public class ServerTest {

	private static int port = 8888;
	
	public static void main(String[] args) throws IOException {
		UserService userService = new UserServiceImpl();
		
		Set<String> exclusions = new HashSet<>();
		exclusions.add("getMap");
		
		RpcProvider.interceptorChain.addLast("methodFilter",
				new MethodFilterInterceptor(exclusions));
		
		RpcProvider.publish(userService, port);
	}
}

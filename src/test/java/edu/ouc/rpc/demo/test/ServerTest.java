package edu.ouc.rpc.demo.test;

import java.io.IOException;

import edu.ouc.rpc.RpcBuilder;
import edu.ouc.rpc.demo.service.UserService;
import edu.ouc.rpc.demo.service.UserServiceImpl;

public class ServerTest {

	private static int port = 8888;
	
	public static void main(String[] args) throws IOException {
		UserService userService = new UserServiceImpl();
		RpcBuilder.buildRpcServer(userService,port);
	}
}

package edu.ouc.rpc.demo.test;

import edu.ouc.rpc.RpcBuilder;
import edu.ouc.rpc.demo.service.User;
import edu.ouc.rpc.demo.service.UserService;

public class ClientTest {

	private static String host = "127.0.0.1";
	private static int port = 8888;
	
	public static void main(String[] args) {
		UserService userService = (UserService) RpcBuilder.buildRpcClient(UserService.class, host, port);
		
		Object msg = null;
		try{
//			msg = userService.test();
//			msg = userService.exceptionTest();
//			msg = userService.queryUserById(0);
//			if(msg instanceof User){
//				System.out.println("parent:" + ((User)msg).getName());
//				System.out.println("child:" + ((User)msg).getChilds().get(0).getName());
//			}
//			System.out.println("msg:" + msg);
			
			userService.timeoutTest();
			
		}catch(Exception e){
			System.out.println("errorMsg:" + e);
		}
	}
}

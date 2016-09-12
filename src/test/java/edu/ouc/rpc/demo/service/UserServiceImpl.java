package edu.ouc.rpc.demo.service;

import edu.ouc.rpc.RpcException;

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
	public void timeoutTest() {
		try {
			//模拟长时间执行
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {}
	}
}

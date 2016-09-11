package edu.ouc.rpc.demo.service;

import java.io.IOException;

import edu.ouc.rpc.RpcException;

/**
 * 测试用业务接口
 * 
 * @author wqx
 *
 */
public interface UserService {
	
	/**
	 * 基本链路测试
	 * 
	 * @return
	 */
	public String test();
	
	/**
	 * 自定义业务类型测试
	 * 
	 * @param userId
	 * @return
	 */
	public User queryUserById(int userId);
	
	/**
	 * 异常测试
	 * 
	 * @throws IOException
	 */
	public Object exceptionTest() throws RpcException;
}

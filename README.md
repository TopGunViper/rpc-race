# rpc-race
一个简单的RPC框架

RPC（Remote Procedure Call ）——远程过程调用，它是一种通过网络从远程计算机程序上请求服务，
而不需要了解底层网络技术的协议。RPC协议假定某些传输协议的存在，如TCP或UDP，为通信程序之间携带信息数据。
在OSI网络通信模型中，RPC跨越了传输层和应用层。RPC使得开发包括网络分布式多程序在内的应用程序更加容易。

基本要求来自于：阿里巴巴中间15年中间件性能挑战赛，要求如下：

1.要成为框架：对于框架的使用者，隐藏RPC实现。

2.网络模块可以自己编写，如果要使用IO框架，要求使用netty-4.0.23.Final。

3.能够传输基本类型、自定义业务类型、异常类型（要在客户端抛出）。

4.支持异步调用，提供future、callback的能力。

5.要处理超时场景，服务端处理时间较长时，客户端在指定时间内跳出本次调用。

6.提供RPC上下文，客户端可以透传数据给服务端。

7.提供Hook，让开发人员进行RPC层面的AOP。


#### 传输基本类型、自定义业务类型、异常类型（要在客户端抛出）
测试用例：
```
// client端
public class ClientTest {

	private static String host = "127.0.0.1";
	private static int port = 8888;
	
	public static void main(String[] args) {
		
		UserService userService = (UserService) RpcBuilder.buildRpcClient(UserService.class, host, port);
		
		Object msg = null;
		try{
			msg = userService.test();
			System.out.println("msg:" + msg);
		}catch(Exception e){
			System.out.println("errorMsg:" + e);
		}
	}
}
//Server端
public class ServerTest {

	private static int port = 8888;
	
	public static void main(String[] args) throws IOException {
		UserService userService = new UserServiceImpl();
		RpcBuilder.buildRpcServer(userService,port);
	}
}
测试用的业务接口UserService:
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

业务实现UserServiceImpl类：

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
}
/**
 * 测试用的自定义业务类型
 * 
 * @author wqx
 *
 */
public class User implements java.io.Serializable{
	
	private static final long serialVersionUID = 493399440916323966L;

	private Integer id;
	
	private String name;
	
	private List<User> childs;

	
	public void addChild(User child){
		if(childs == null){
			childs = new ArrayList<User>();
		}
		childs.add(child);
	}
	//。。。getter setter 
}
```

测试test方法：预期输出：
```
msg : hello client, this is rpc server.
```
测试exceptionTest方法：

```
msg = userService.exceptionTest();
输出：
errorMsg:edu.ouc.rpc.RpcException: exception occur in server！！！
```
测试queryUserById方法：
```
msg = userService.queryUserById(0);
if(msg instanceof User){
	System.out.println("parent:" + ((User)msg).getName());
	System.out.println("child:" + ((User)msg).getChilds().get(0).getName());
}
输出：
parent:小明爸爸
child:小明同学
```

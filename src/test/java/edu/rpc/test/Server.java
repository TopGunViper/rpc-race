package edu.rpc.test;

import com.alibaba.middleware.race.rpc.demo.service.RaceTestService;
import com.alibaba.middleware.race.rpc.demo.service.RaceTestServiceImpl;

import io.craft.atom.rpc.api.RpcFactory;
import io.craft.atom.rpc.api.RpcParameter;
import io.craft.atom.rpc.api.RpcServer;

public class Server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int port = 8888;
		RpcServer server = RpcFactory.newRpcServer(port);
		server.export(RaceTestService.class, new RaceTestServiceImpl(), new RpcParameter(10, 100));
		server.open();
	}

}

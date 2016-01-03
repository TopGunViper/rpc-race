package edu.rpc.test;

import com.alibaba.middleware.race.rpc.demo.service.RaceTestService;

import io.craft.atom.rpc.api.RpcClient;
import io.craft.atom.rpc.api.RpcFactory;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String host = "127.0.0.1";
		int port = 8888;
		
		RpcClient client = RpcFactory.newRpcClient(host, port);
		client.open();
		RaceTestService demo = client.refer(RaceTestService.class);
		demo.getString();
	}

}

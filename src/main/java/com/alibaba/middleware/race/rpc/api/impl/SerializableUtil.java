package com.alibaba.middleware.race.rpc.api.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.alibaba.middleware.race.rpc.model.RpcRequest;

import de.ruedigermoeller.serialization.FSTConfiguration;

/**
 * 序列化和反序列化
 * 1.JDK原生方法
 * 2.FST
 * 3.Kryo
 * 
 * @author wqx
 *
 */
public class SerializableUtil {

	/**
	 * JDK系列化
	 * 
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static byte[] serializeObject(Object object) throws IOException{

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(object);
		oos.flush();
		return baos.toByteArray();
	}
	/** 
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 * */
	public static Object deserializeObject(byte[]buf) throws IOException, ClassNotFoundException{
		Object object=null;
		ByteArrayInputStream sais=new ByteArrayInputStream(buf);
		ObjectInputStream ois = new ObjectInputStream(sais);
		object=ois.readObject();
		return object;

	}
	/**
	 * FST(fast-serialization)是从新实现的Java快速对象序列化的框架，完全兼容JDK原生序列化协议。
	 * 序列化速度是JDK的4-10倍
	 */
	static FSTConfiguration configuration = FSTConfiguration
			.createStructConfiguration();

	public static byte[] FSTserialize(Object obj) {
		return configuration.asByteArray((Serializable) obj);
	}
	public static Object FSTdeserialize(byte[] sec) {
		return configuration.asObject(sec);
	}

//	
//	static Kryo kryo = new Kryo();
//	public static byte[] serialize(Object obj){
//		byte[] buffer = new byte[2048];
//		try(
//				Output output = new Output(buffer);
//				) {
//			kryo.writeClassAndObject(output, obj);
//			return output.toBytes();
//		} catch (Exception e) {
//		}
//		return buffer;
//	}
//
//	public static Object deserialize(byte[] src) {
//		try(
//				Input input = new Input(src);
//				){
//			return kryo.readClassAndObject(input);
//		}catch (Exception e) {
//		}
//		return kryo;
//	}
	public static void main(String[] args) throws Exception
	{
		Class<?> [] parameterType = {Integer.class,String.class};
		Object[] parameters = {10,"hello rpc"};
		RpcRequest req = new RpcRequest(SerializableUtil.class,"methodA",parameterType,parameters);
		
		RpcRequest req_jdk = (RpcRequest)deserializeObject(serializeObject(req));
		System.out.println("req_jdk,methodName:" + req_jdk.getMethodName());
		RpcRequest req_fst = (RpcRequest)deserializeObject(serializeObject(req));
		System.out.println("req_fst,methodName:" + req_jdk.getMethodName());
		
	}
}

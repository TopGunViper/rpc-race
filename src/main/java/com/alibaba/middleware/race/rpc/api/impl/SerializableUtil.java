package com.alibaba.middleware.race.rpc.api.impl;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class SerializableUtil {

//	static FSTConfiguration configuration = FSTConfiguration
//			.createStructConfiguration();
//
//	public static byte[] FSTserialize(Object obj) {
//		return configuration.asByteArray(obj);
//	}
//	public static Object FSTdeserialize(byte[] sec) {
//		return configuration.asObject(sec);
//	}
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


	public static byte[] serializeObject(Object object) throws IOException{

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(object);
		oos.flush();
		return baos.toByteArray();
	}

	/** 

	 * @throws IOException

	 * @throws ClassNotFoundException */

	public static Object deserializeObject(byte[]buf) throws IOException, ClassNotFoundException{
		Object object=null;
		ByteArrayInputStream sais=new ByteArrayInputStream(buf);
		ObjectInputStream ois = new ObjectInputStream(sais);
		object=ois.readObject();
		return object;

	}
}

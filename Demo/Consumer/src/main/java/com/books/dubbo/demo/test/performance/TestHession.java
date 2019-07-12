package com.books.dubbo.demo.test.performance;

import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.dubbo.common.serialize.hessian2.Hessian2Serialization;
import org.apache.dubbo.common.serialize.hessian2.Hessian2SerializerFactory;

public class TestHession implements Serializable {

	public static void main(String[] args) throws InterruptedException {

		Integer i = null;
		int ii = i;
		
		
		ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2, 1, TimeUnit.SECONDS	, new ArrayBlockingQueue<>(10));
		
		executor.setCorePoolSize(10);
		
		SynchronousQueue<String> synQ = new SynchronousQueue<>();
		System.out.println(synQ.offer("abc"));
		synQ.put("abc");
		
		
		Hessian2Serialization  h = new Hessian2Serialization();
		Hessian2SerializerFactory.SERIALIZER_FACTORY.setAllowNonSerializable(true);
		
		Double price = 39.9;
		System.out.println((1-0.3)*(1-0.1)*price*0.5);
		
	}

}

package com.books.dubbo.demo.api;

public interface GreetingService {
	String sayHello(String name);
	
	Result<String> testGeneric(PoJo poJo);
}

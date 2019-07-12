package com.books.dubbo.demo.api;

import com.books.dubbo.demo.api.GreetingService;
import com.books.dubbo.demo.api.PoJo;
import com.books.dubbo.demo.api.Result;

public class GreetingServiceMock implements GreetingService{

	@Override
	public String sayHello(String name) {
		return "mock value";
	}

	@Override
	public Result<String> testGeneric(PoJo poJo) {
		return null;
	}
}

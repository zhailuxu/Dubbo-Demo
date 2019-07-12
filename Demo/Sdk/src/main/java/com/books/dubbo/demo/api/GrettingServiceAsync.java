package com.books.dubbo.demo.api;

import java.util.concurrent.CompletableFuture;
 
public interface GrettingServiceAsync {
	CompletableFuture<String> sayHello(String name);
}
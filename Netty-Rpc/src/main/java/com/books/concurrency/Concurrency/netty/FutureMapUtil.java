package com.books.concurrency.Concurrency.netty;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class FutureMapUtil {
	// <请求id，对应的future>
	private static final ConcurrentHashMap<String, CompletableFuture> futureMap = new ConcurrentHashMap<String, CompletableFuture>();
 
	public static void put(String id, CompletableFuture future) {
		futureMap.put(id, future);
	}

	public static CompletableFuture remove(String id) {
		return futureMap.remove(id);
	}
}

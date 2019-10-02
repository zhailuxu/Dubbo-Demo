package com.books.concurrency.Concurrency.netty;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TestModelAsyncRpc {

	private static final RpcClient rpcClient = new RpcClient();

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		// 1.同步调用
		System.out.println(rpcClient.rpcSyncCall("who are you"));

		// 2.发起远程调用异步，并注册回调，马上返回
		CompletableFuture<String> future = rpcClient.rpcAsyncCall("who are you");
		
		future.whenComplete((v, t) -> {
			if (t != null) {
				t.printStackTrace();
			} else {
				System.out.println(v);
			}

		});

		System.out.println("---async rpc call over");

	}
}

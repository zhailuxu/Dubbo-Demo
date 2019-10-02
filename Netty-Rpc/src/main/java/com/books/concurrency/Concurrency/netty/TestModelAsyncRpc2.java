package com.books.concurrency.Concurrency.netty;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TestModelAsyncRpc2 {

	private static final RpcClient rpcClient = new RpcClient();

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		// 1.发起远程调用异步，并注册回调，马上返回
		CompletableFuture<String> future1 = rpcClient.rpcAsyncCall("who are you");
		// 2.发起远程调用异步，并注册回调，马上返回
		CompletableFuture<String> future2 = rpcClient.rpcAsyncCall("who are you");

		// 3.等两个请求都返回结果时候，使用结果做些事情
		CompletableFuture<String> future = future1.thenCombine(future2, (u, v) -> {

			return u + v;
		});

		// 4.等待最终结果
		future.whenComplete((v, t) -> {
			if (t != null) {
				t.printStackTrace();
			} else {
				System.out.println(v);
			}

		});
		System.out.println("---async rpc call over---");
		// rpcClient.close();

	}

}

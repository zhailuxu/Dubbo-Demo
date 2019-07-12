package org.apache.dubbo.demo.Future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class TestFuture {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//System.out.println("async return ");

			return "over";
		});
		
		result = result.thenApplyAsync(new Function<String, String>() {

			@Override
			public String apply(String t) {
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("thenapply");
				return t;
			}
		});
		System.out.println("end apply");
		System.out.println(result.get());
		
		Thread.sleep(200000);
		
		

	}

}

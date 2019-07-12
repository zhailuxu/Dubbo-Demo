package org.apache.dubbo.demo.consumer;

import java.util.concurrent.CompletableFuture;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;

import com.books.dubbo.demo.api.GreetingService;
import com.books.dubbo.demo.api.GrettingServiceAsync;

public class APiConsumerForProviderAsync {
	public static void main(String[] args) throws InterruptedException {
		//1.创建服务引用实例，并设置
		ReferenceConfig<GrettingServiceAsync> referenceConfig = new ReferenceConfig<GrettingServiceAsync>();
		referenceConfig.setApplication(new ApplicationConfig("first-dubbo-consumer"));
		referenceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
		referenceConfig.setInterface(GrettingServiceAsync.class);
		referenceConfig.setTimeout(5000);
		//referenceConfig.setCluster("myCluster");

		referenceConfig.setVersion("1.0.0");
		referenceConfig.setGroup("dubbo");
		
		//2.服务引用
		GrettingServiceAsync greetingService = referenceConfig.get();
		
		//3.设置隐士参数
		RpcContext.getContext().setAttachment("company", "alibaba");
		//RpcContext.getContext().set("ip", "30.39.148.197");
		
		//4.获取future并设置回调
		CompletableFuture<String> future = greetingService.sayHello("world");
		future.whenComplete((v, t) -> {
			if (t != null) {
				t.printStackTrace();
			} else {
				System.out.println(v);
			}

		});
		
		Thread.currentThread().join();

	}
}     
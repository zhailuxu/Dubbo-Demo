package org.apache.dubbo.demo.consumer;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;

import com.books.dubbo.demo.api.GreetingService;

public class APiConsumerParamer {
	public static void main(String[] args) {
		ReferenceConfig<GreetingService> referenceConfig = new ReferenceConfig<GreetingService>();
		referenceConfig.setApplication(new ApplicationConfig("first-dubbo-consumer"));
		referenceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
		referenceConfig.setInterface(GreetingService.class);

		referenceConfig.setVersion("1.0.0");
		referenceConfig.setGroup("dubbo");
		
		GreetingService greetingService = referenceConfig.get();

		// 设置隐式参数
		RpcContext.getContext().setAttachment("company", "alibaba");

		System.out.println(greetingService.sayHello("world"));
	}
}
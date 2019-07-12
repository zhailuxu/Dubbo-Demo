package org.apache.dubbo.demo.consumer;

import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.MethodConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;

import com.books.dubbo.demo.api.GreetingService;

public class APiConsumerForActiveLimit {
	public static void main(String[] args) {
		ReferenceConfig<GreetingService> referenceConfig = new ReferenceConfig<GreetingService>();
		referenceConfig.setApplication(new ApplicationConfig("first-dubbo-consumer"));
		referenceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
		referenceConfig.setInterface(GreetingService.class);

		referenceConfig.setVersion("1.0.0");
		referenceConfig.setGroup("dubbo");
		
		//设置激活并发限制个数
		referenceConfig.setActives(10);
		
//		final List<MethodConfig> methodList = new ArrayList<MethodConfig>();
//
//		MethodConfig methodConfig = new MethodConfig();
//		methodConfig.setActives(10);
//		methodConfig.setName("sayHello");
//		methodList.add(methodConfig);
//		referenceConfig.setMethods(methodList);
		
		
		GreetingService greetingService = referenceConfig.get();
		
		//设置隐式参数
		RpcContext.getContext().setAttachment("company", "alibaba");
		
		
		System.out.println(greetingService.sayHello("world"));
	}
}     
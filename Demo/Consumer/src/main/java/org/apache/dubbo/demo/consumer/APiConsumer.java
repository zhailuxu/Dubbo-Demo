package org.apache.dubbo.demo.consumer;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;

import com.books.dubbo.demo.api.GreetingService;

public class APiConsumer {
	public static void main(String[] args) throws InterruptedException {
		
		
		
		// 10.创建服务引用对象实例
		ReferenceConfig<GreetingService> referenceConfig = new ReferenceConfig<GreetingService>();
		// 11.设置应用程序信息
		referenceConfig.setApplication(new ApplicationConfig("first-dubbo-consumer"));
		// 12.设置服务注册中心
		referenceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
		
		//直连测试
		//referenceConfig.setUrl("dubbo://192.168.0.109:20880");
		
		// 13.设置服务接口和超时时间
		referenceConfig.setInterface(GreetingService.class);
		referenceConfig.setTimeout(5000);
		
		// 14.设置自定义负载均衡策略与集群容错策略
		 referenceConfig.setLoadbalance("myroundrobin");
		 referenceConfig.setCluster("myCluster");
		 RpcContext.getContext().set("ip", "30.10.67.231");

		// 15.设置服务分组与版本
		referenceConfig.setVersion("1.0.0");
		referenceConfig.setGroup("dubbo");

		// 16.引用服务
		GreetingService greetingService = referenceConfig.get();

		// 17. 设置隐式参数
		RpcContext.getContext().setAttachment("company", "alibaba");

		// 18调用服务
		System.out.println(greetingService.sayHello("world"));
		
		Thread.currentThread().join();
	}
}
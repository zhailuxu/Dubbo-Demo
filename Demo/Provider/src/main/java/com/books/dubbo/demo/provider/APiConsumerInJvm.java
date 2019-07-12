package com.books.dubbo.demo.provider;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.rpc.RpcContext;

import com.books.dubbo.demo.api.GreetingService;
import com.books.dubbo.demo.provider.GreetingServiceImpl;

public class APiConsumerInJvm {

	static public void exportService() {
		// 1.创建ServiceConfig实例
		ServiceConfig<GreetingService> serviceConfig = new ServiceConfig<GreetingService>();
		// 2.设置应用程序配置
		serviceConfig.setApplication(new ApplicationConfig("first-dubbo-provider"));

		// 3.设置服务注册中心信息
		RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");
		serviceConfig.setRegistry(registryConfig);
		// 4.设置接口与实现类
		serviceConfig.setInterface(GreetingService.class);
		serviceConfig.setRef(new GreetingServiceImpl());

		// 5.设置服务分组与版本
		serviceConfig.setVersion("1.0.0");
		serviceConfig.setGroup("dubbo");

		// 6.设置线程池策略

		// 7.导出服务
		serviceConfig.export();

		// 8.挂起线程，避免服务停止
		System.out.println("server is started");
	}

	static public void referService() {
		// 10.创建服务引用对象实例
		ReferenceConfig<GreetingService> referenceConfig = new ReferenceConfig<GreetingService>();
		// 12.设置服务注册中心
		referenceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));

		// 13.设置服务接口和超时时间
		referenceConfig.setInterface(GreetingService.class);
		referenceConfig.setTimeout(5000);
		referenceConfig.setAsync(true);

		referenceConfig.setVersion("1.0.0");
		referenceConfig.setGroup("dubbo");
		referenceConfig.setCheck(false);

		// 16.引用服务
		GreetingService greetingService = referenceConfig.get();

		// 17. 设置隐式参数
		RpcContext.getContext().setAttachment("company", "alibaba");

		// 18调用服务
		System.out.println(greetingService.sayHello("world"));
	}

	public static void main(String[] args) throws InterruptedException {
		// 导出服务
		exportService();

		// 引用服务
		referService();

		Thread.currentThread().join();
	}
}
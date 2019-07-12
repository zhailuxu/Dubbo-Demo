package org.apache.dubbo.demo.consumer;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;

import com.books.dubbo.demo.api.GreetingService;

public class APiConsumerMock {
	public static void main(String[] args) throws InterruptedException {
		// 0.创建服务引用对象实例
		ReferenceConfig<GreetingService> referenceConfig = new ReferenceConfig<GreetingService>();
		// 1.设置应用程序信息
		referenceConfig.setApplication(new ApplicationConfig("first-dubbo-consumer"));
		// 2.设置服务注册中心
		referenceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
		// 3.设置服务接口和超时时间
		referenceConfig.setInterface(GreetingService.class);
		referenceConfig.setTimeout(5000);

		// 4.设置服务分组与版本
		referenceConfig.setVersion("1.0.0");
		referenceConfig.setGroup("dubbo");

		// 5设置启动时候不检查服务提供者是否可用
		referenceConfig.setCheck(false);
		referenceConfig.setMock("true");
		// 6.引用服务
		GreetingService greetingService = referenceConfig.get();

		// 7. 设置隐式参数
		RpcContext.getContext().setAttachment("company", "alibaba");

		// 8调用服务
		System.out.println(greetingService.sayHello("world"));

	}
}
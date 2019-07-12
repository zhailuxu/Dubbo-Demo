package org.apache.dubbo.demo.consumer;

import java.io.IOException;

import org.apache.dubbo.common.json.JSON;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;

import com.books.dubbo.demo.api.GreetingService;
import com.books.dubbo.demo.find.iplist.ZookeeperIpList;

public class APiConsumerSetIpcall {
	public static void main(String[] args) throws InterruptedException {

		// 1.创建服务引用对象实例
		ReferenceConfig<GreetingService> referenceConfig = new ReferenceConfig<GreetingService>();
		// 2.设置应用程序信息
		referenceConfig.setApplication(new ApplicationConfig("first-dubbo-consumer"));
		// 3.设置服务注册中心
		referenceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));

		// 4.设置服务接口和超时时间
		referenceConfig.setInterface(GreetingService.class);
		referenceConfig.setTimeout(5000);

		// 5.设置自定义负载均衡策略与集群容错策略（以便实现指定ip）
		referenceConfig.setCluster("myCluster");

		// 6.设置服务分组与版本
		referenceConfig.setVersion("1.0.0");
		referenceConfig.setGroup("dubbo");

		// 7.引用服务
		GreetingService greetingService = referenceConfig.get();

		//8.获取地址列表
		ZookeeperIpList zk = new ZookeeperIpList();
		zk.init("127.0.0.1:2181", "dubbo", "com.books.dubbo.demo.api.GreetingService:1.0.0", "dubbo");

		//9.指定ip调用
		for (String ip : zk.getIpList()) {
			RpcContext.getContext().set("ip", ip);
			System.out.println(greetingService.sayHello("world" + ip));
		}

	}
}
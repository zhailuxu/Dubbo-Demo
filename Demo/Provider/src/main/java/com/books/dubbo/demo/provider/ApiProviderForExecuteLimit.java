package com.books.dubbo.demo.provider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.MethodConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

import com.books.dubbo.demo.api.GreetingService;

public class ApiProviderForExecuteLimit {

	public static void main(String[] args) throws IOException {

		ServiceConfig<GreetingService> serviceConfig = new ServiceConfig<GreetingService>();
		serviceConfig.setApplication(new ApplicationConfig("first-dubbo-provider"));
		serviceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
		serviceConfig.setInterface(GreetingService.class);
		serviceConfig.setRef(new GreetingServiceImpl());
		
		serviceConfig.setVersion("1.0.0");
		serviceConfig.setGroup("dubbo");
		
		//设置并发控制数
		serviceConfig.setExecutes(10);

		final List<MethodConfig> methodList = new ArrayList<MethodConfig>();

		MethodConfig methodConfig = new MethodConfig();
		methodConfig.setExecutes(10);
		methodConfig.setName("sayHello");
		methodList.add(methodConfig);
		serviceConfig.setMethods(methodList);

		// 设置线程池策略
		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("threadpool", "mythreadpool");
		serviceConfig.setParameters(parameters);

		serviceConfig.export();

		System.out.println("server is started");
		System.in.read();
	}
}

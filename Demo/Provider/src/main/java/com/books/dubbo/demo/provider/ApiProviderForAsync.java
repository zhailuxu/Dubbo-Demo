package com.books.dubbo.demo.provider;

import java.io.IOException;
import java.util.HashMap;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

import com.books.dubbo.demo.api.GreetingService;
import com.books.dubbo.demo.api.GrettingServiceAsync;

public class ApiProviderForAsync {

	public static void main(String[] args) throws IOException {

		// 1.创建服务发布实例，并设置
		ServiceConfig<GrettingServiceAsync> serviceConfig = new ServiceConfig<GrettingServiceAsync>();
		serviceConfig.setApplication(new ApplicationConfig("first-dubbo-provider"));
		serviceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
		serviceConfig.setInterface(GrettingServiceAsync.class);
		serviceConfig.setRef(new GrettingServiceAsyncImpl());
		serviceConfig.setVersion("1.0.0");
		serviceConfig.setGroup("dubbo");

		// 2.设置线程池策略
		// HashMap<String, String> parameters = new HashMap<>();
		// parameters.put("threadpool", "mythreadpool");
		// serviceConfig.setParameters(parameters);

		// 3.导出服务
		serviceConfig.export();

		// 4.阻塞线程
		System.out.println("server is started");
		System.in.read();
	}
}

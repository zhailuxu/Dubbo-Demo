package org.apache.dubbo.demo.consumer;

import static org.apache.dubbo.common.Constants.CATEGORY_KEY;
import static org.apache.dubbo.common.Constants.CONFIGURATORS_CATEGORY;
import static org.apache.dubbo.common.Constants.PROVIDERS_CATEGORY;
import static org.apache.dubbo.common.Constants.ROUTERS_CATEGORY;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.registry.Registry;
import org.apache.dubbo.registry.RegistryFactory;
import org.apache.dubbo.rpc.RpcContext;

import com.books.dubbo.demo.api.GreetingService;

public class APiConsumerMockResult {

	public static void mockResult(String type) {
		// (1)获取服务注册中心工厂
		RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class)
				.getAdaptiveExtension();
		// (2)根据zk地址，获取具体的zk注册中心的客户端实例
		Registry registry2 = registryFactory.getRegistry(URL.valueOf("zookeeper://127.0.0.1:2181"));

		// directory.subscribe(subscribeUrl.addParameter(CATEGORY_KEY,
		// PROVIDERS_CATEGORY + "," + CONFIGURATORS_CATEGORY + "," + ROUTERS_CATEGORY));

		// (3)注册降级方案到zk
		registry2.register(URL.valueOf(
				"override://0.0.0.0/com.books.dubbo.demo.api.GreetingService?category=configurators&dynamic=false&application=first-dubbo-consumer&"
						+ "mock=" + type + ":return+null&group=dubbo&version=1.0.0"));

		//(4)取消配置
//		registry2.unregister(URL.valueOf(
//				"override://0.0.0.0/com.books.dubbo.demo.api.GreetingService?category=configurators&dynamic=false&application=first-dubbo-consumer&"
//						+ "mock=" + type + ":return+null&group=dubbo&version=1.0.0"));
	}

	public static void main(String[] args) throws InterruptedException {

		// mock=force:result+null;
		mockResult("force");

		// mock=fail:result+null;
		// mockResult("fail");

	}
}
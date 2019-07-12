package org.apache.dubbo.demo.consumer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.io.UnsafeByteArrayOutputStream;
import org.apache.dubbo.common.json.JSON;
import org.apache.dubbo.common.serialize.Serialization;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;

import com.books.dubbo.demo.api.GreetingService;

public class APiGenericConsumerForTrue {
	public static void main(String[] args) throws IOException {
		// 1.泛型参数固定为GenericService
		ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<GenericService>();
		referenceConfig.setApplication(new ApplicationConfig("first-dubbo-consumer"));
		referenceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));

		referenceConfig.setVersion("1.0.0");
		referenceConfig.setGroup("dubbo");

		// 2. 设置为泛化引用，类型为true
		referenceConfig.setInterface("com.books.dubbo.demo.api.GreetingService");
		referenceConfig.setGeneric(true);

		// 3.用org.apache.dubbo.rpc.service.GenericService替代所有接口引用
		GenericService greetingService = referenceConfig.get();

		// 4.泛型调用， 基本类型以及Date,List,Map等不需要转换，直接调用,如果返回值为POJO也将自动转成Map
		Object result = greetingService.$invoke("sayHello", new String[] { "java.lang.String" },
				new Object[] { "world" });

		System.out.println(JSON.json(result));

		// 5. POJO参数转换为map
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("class", "com.books.dubbo.demo.api.PoJo");
		map.put("id", "1990");
		map.put("name", "jiaduo");

		// 6.发起泛型调用
		result = greetingService.$invoke("testGeneric", new String[] { "com.books.dubbo.demo.api.PoJo" },
				new Object[] { map });
		System.out.println((result));
	}
}
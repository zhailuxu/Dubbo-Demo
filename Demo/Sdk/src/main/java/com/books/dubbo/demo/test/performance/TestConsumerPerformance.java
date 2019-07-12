
package com.books.dubbo.demo.test.performance;

import java.util.UUID;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.books.dubbo.demo.api.GreetingService;

public class TestConsumerPerformance extends AbstractJavaSamplerClient {

	private GreetingService greetingService;

	@Override
	public SampleResult runTest(JavaSamplerContext arg0) {

		System.out.println("---begin run----");
		// 0.结果对象
		SampleResult sr = new SampleResult();

		String result = null;
		// 1.启动测试
		try {

			// 1.1开启样本测试
			sr.sampleStart();
			// 1.2发起远程调用
			result = greetingService.sayHello(UUID.randomUUID().toString());
			// 1.3设置响应结果哦哦
			sr.setResponseData("from provider:" + result, null);
			sr.setDataType(SampleResult.TEXT);
			sr.setSuccessful(true);

			// 1.4关闭样本测试
			sr.sampleEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("---end run----" + result);
		// 2.返回结果
		return sr;
	}

	// 创建引入实例
	@Override
	public void setupTest(JavaSamplerContext context) {
		
		try {
			// 0.创建服务引用对象实例
			ReferenceConfig<GreetingService> referenceConfig = new ReferenceConfig<GreetingService>();
			// 1.设置应用程序信息
			referenceConfig.setApplication(new ApplicationConfig("first-dubbo-consumer"));
			// 2.设置服务注册中心
			referenceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
			// 3.设置服务接口和超时时间
			referenceConfig.setInterface(GreetingService.class);
			referenceConfig.setTimeout(5000);
			// 4.设置自定义负载均衡策略与集群容错策略

			// 5.设置服务分组与版本
			referenceConfig.setVersion("1.0.0");
			referenceConfig.setGroup("dubbo");

			// 6.引用服务
			greetingService = referenceConfig.get();

			// 76调用服务
			System.out.println("get referenc ok ");
		}catch(Exception e) {
			System.out.println(e.getLocalizedMessage());

		}
		
	}

	public static void main(String[] ar) {
		TestConsumerPerformance test = new TestConsumerPerformance();
		test.setupTest(null);
		test.runTest(null);

	}
}

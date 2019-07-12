package org.apache.dubbo.demo.loadbalance;

import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcInvocation;

public class TestRoundRobinLoadBalance {

	public static void main(String[] args) {
		//1.mock三个invoker，并设置权重
		MyInvoker invoker1 = new MyInvoker("A", URL.valueOf(
				"dubbo://30.10.76.1:20880/com.books.dubbo.demo.api.GreetingService?anyhost=true&application=first-dubbo-consumer&check=false&default.deprecated=false&default.dynamic=false&default.lazy=false&default.register=true&default.sticky=false&deprecated=false&dubbo=2.0.2&dynamic=false&generic=false&interface=com.books.dubbo.demo.api.GreetingService&lazy=false&loadbalance=roundrobin&methods=sayHello&pid=11215&register=true&register.ip=30.10.67.100&release=2.7.1&remote.application=first-dubbo-provider&remote.timestamp=1555035745116&side=consumer&sticky=false&timestamp=1555158305821&weight=1"));
		MyInvoker invoker2 = new MyInvoker("B", URL.valueOf(
				"dubbo://30.10.76.2:20880/com.books.dubbo.demo.api.GreetingService?anyhost=true&application=first-dubbo-consumer&check=false&default.deprecated=false&default.dynamic=false&default.lazy=false&default.register=true&default.sticky=false&deprecated=false&dubbo=2.0.2&dynamic=false&generic=false&interface=com.books.dubbo.demo.api.GreetingService&lazy=false&loadbalance=roundrobin&methods=sayHello&pid=11215&register=true&register.ip=30.10.67.100&release=2.7.1&remote.application=first-dubbo-provider&remote.timestamp=1555035745116&side=consumer&sticky=false&timestamp=1555158305821&weight=1"));
		MyInvoker invoker3 = new MyInvoker("C", URL.valueOf(
				"dubbo://30.10.76.3:20880/com.books.dubbo.demo.api.GreetingService?anyhost=true&application=first-dubbo-consumer&check=false&default.deprecated=false&default.dynamic=false&default.lazy=false&default.register=true&default.sticky=false&deprecated=false&dubbo=2.0.2&dynamic=false&generic=false&interface=com.books.dubbo.demo.api.GreetingService&lazy=false&loadbalance=roundrobin&methods=sayHello&pid=11215&register=true&register.ip=30.10.67.100&release=2.7.1&remote.application=first-dubbo-provider&remote.timestamp=1555035745116&side=consumer&sticky=false&timestamp=1555158305821&weight=1"));

		List<Invoker<String>> list = new ArrayList<Invoker<String>>();
		list.add(invoker1);
		list.add(invoker2);
		list.add(invoker3);
        
		//2.创建mock的RoundRobin
		MyRoundRobinLoadBalance loadBalance = new MyRoundRobinLoadBalance();
		
		//3.模拟远程调用，使用RoundRobin来做机器选择
		RpcInvocation invocation = new RpcInvocation();
		invocation.setMethodName("sayHello");
		for (int i = 0; i < 12; ++i) {

			MyInvoker selectInvoker = (MyInvoker) loadBalance.doSelect(list, null, invocation);
			System.out.println(selectInvoker.getProviderId());
		}
	}

}

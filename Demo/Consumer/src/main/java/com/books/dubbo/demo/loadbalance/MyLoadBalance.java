package com.books.dubbo.demo.loadbalance;
import java.util.List;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;

public class MyLoadBalance extends AbstractLoadBalance {

	@Override
	protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {

		Invoker invoker = null;
		// 自定义负载均衡算法，从invokers中选择一个invoker
		invoker = invokers.get(0);
		return invoker;
	}
}
package org.apache.dubbo.demo.loadbalance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcInvocation;
import org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;

public class MyRoundRobinLoadBalance extends AbstractLoadBalance {

	public static final String NAME = "roundrobin";

	private static int RECYCLE_PERIOD = 60000;

	protected static class WeightedRoundRobin {
		private int weight;
		private AtomicLong current = new AtomicLong(0);
		private long lastUpdate;

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
			current.set(0);
		}

		public long increaseCurrent() {
			return current.addAndGet(weight);
		}

		public void sel(int total) {
			current.addAndGet(-1 * total);
		}

		public long getLastUpdate() {
			return lastUpdate;
		}

		public void setLastUpdate(long lastUpdate) {
			this.lastUpdate = lastUpdate;
		}
	}

	private ConcurrentMap<String, ConcurrentMap<String, WeightedRoundRobin>> methodWeightMap = new ConcurrentHashMap<String, ConcurrentMap<String, WeightedRoundRobin>>();
	private AtomicBoolean updateLock = new AtomicBoolean();

	/**
	 * get invoker addr list cached for specified invocation
	 * <p>
	 * <b>for unit test only</b>
	 * 
	 * @param invokers
	 * @param invocation
	 * @return
	 */
	protected <T> Collection<String> getInvokerAddrList(List<Invoker<T>> invokers, Invocation invocation) {
		String key = invokers.get(0).getUrl().getServiceKey() + "." + invocation.getMethodName();
		Map<String, WeightedRoundRobin> map = methodWeightMap.get(key);
		if (map != null) {
			return map.keySet();
		}
		return null;
	}

	@Override
	protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
		String key = invokers.get(0).getUrl().getServiceKey() + "." + invocation.getMethodName();
		ConcurrentMap<String, WeightedRoundRobin> map = methodWeightMap.get(key);
		if (map == null) {
			methodWeightMap.putIfAbsent(key, new ConcurrentHashMap<String, WeightedRoundRobin>());
			map = methodWeightMap.get(key);
		}
		int totalWeight = 0;
		long maxCurrent = Long.MIN_VALUE;
		long now = System.currentTimeMillis();
		Invoker<T> selectedInvoker = null;
		WeightedRoundRobin selectedWRR = null;
		for (Invoker<T> invoker : invokers) {
			String identifyString = invoker.getUrl().toIdentityString();
			WeightedRoundRobin weightedRoundRobin = map.get(identifyString);
			int weight = getWeight(invoker, invocation);

			if (weightedRoundRobin == null) {
				weightedRoundRobin = new WeightedRoundRobin();
				weightedRoundRobin.setWeight(weight);
				map.putIfAbsent(identifyString, weightedRoundRobin);
			}
			if (weight != weightedRoundRobin.getWeight()) {
				// weight changed
				weightedRoundRobin.setWeight(weight);
			}
			long cur = weightedRoundRobin.increaseCurrent();
			weightedRoundRobin.setLastUpdate(now);
			if (cur > maxCurrent) {
				maxCurrent = cur;
				selectedInvoker = invoker;
				selectedWRR = weightedRoundRobin;
			}
			totalWeight += weight;
		}
		if (!updateLock.get() && invokers.size() != map.size()) {
			if (updateLock.compareAndSet(false, true)) {
				try {
					// copy -> modify -> update reference
					ConcurrentMap<String, WeightedRoundRobin> newMap = new ConcurrentHashMap<String, WeightedRoundRobin>();
					newMap.putAll(map);
					Iterator<Entry<String, WeightedRoundRobin>> it = newMap.entrySet().iterator();
					while (it.hasNext()) {
						Entry<String, WeightedRoundRobin> item = it.next();
						if (now - item.getValue().getLastUpdate() > RECYCLE_PERIOD) {
							it.remove();
						}
					}
					methodWeightMap.put(key, newMap);
				} finally {
					updateLock.set(false);
				}
			}
		}
		if (selectedInvoker != null) {
			selectedWRR.sel(totalWeight);
			return selectedInvoker;
		}
		// should not happen here
		return invokers.get(0);
	}

	public static void main(String a) {
		MyInvoker invoker1 = new MyInvoker("1", URL.valueOf(
				"dubbo://30.10.76.9:20880/com.books.dubbo.demo.api.GreetingService?anyhost=true&application=first-dubbo-consumer&check=false&default.deprecated=false&default.dynamic=false&default.lazy=false&default.register=true&default.sticky=false&deprecated=false&dubbo=2.0.2&dynamic=false&generic=false&interface=com.books.dubbo.demo.api.GreetingService&lazy=false&loadbalance=roundrobin&methods=sayHello&pid=11215&register=true&register.ip=30.10.67.100&release=2.7.1&remote.application=first-dubbo-provider&remote.timestamp=1555035745116&side=consumer&sticky=false&timestamp=1555158305821&weight=1"));
		MyInvoker invoker2 = new MyInvoker("2", URL.valueOf(
				"dubbo://30.10.76.9:20880/com.books.dubbo.demo.api.GreetingService?anyhost=true&application=first-dubbo-consumer&check=false&default.deprecated=false&default.dynamic=false&default.lazy=false&default.register=true&default.sticky=false&deprecated=false&dubbo=2.0.2&dynamic=false&generic=false&interface=com.books.dubbo.demo.api.GreetingService&lazy=false&loadbalance=roundrobin&methods=sayHello&pid=11215&register=true&register.ip=30.10.67.100&release=2.7.1&remote.application=first-dubbo-provider&remote.timestamp=1555035745116&side=consumer&sticky=false&timestamp=1555158305821&weight=2"));
		MyInvoker invoker3 = new MyInvoker("3", URL.valueOf(
				"dubbo://30.10.76.9:20880/com.books.dubbo.demo.api.GreetingService?anyhost=true&application=first-dubbo-consumer&check=false&default.deprecated=false&default.dynamic=false&default.lazy=false&default.register=true&default.sticky=false&deprecated=false&dubbo=2.0.2&dynamic=false&generic=false&interface=com.books.dubbo.demo.api.GreetingService&lazy=false&loadbalance=roundrobin&methods=sayHello&pid=11215&register=true&register.ip=30.10.67.100&release=2.7.1&remote.application=first-dubbo-provider&remote.timestamp=1555035745116&side=consumer&sticky=false&timestamp=1555158305821&weight=3"));

		List<Invoker<String>> list = new ArrayList<Invoker<String>>();
		list.add(invoker1);
		list.add(invoker2);
		list.add(invoker3);

		MyRoundRobinLoadBalance loadBalance = new MyRoundRobinLoadBalance();
		RpcInvocation invocation = new RpcInvocation();
		invocation.setMethodName("sayHello");

	}

}
package org.apache.dubbo.demo.cluster;

import java.util.List;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.cluster.LoadBalance;

public abstract class MyAbstractLoadBlance implements LoadBalance {
	static int calculateWarmupWeight(int uptime, int warmup, int weight) {
		int ww = (int) ((float) uptime / ((float) warmup / (float) weight));
		return ww < 1 ? 1 : (ww > weight ? weight : ww);
	}

	@Override
	public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
		if (CollectionUtils.isEmpty(invokers)) {
			return null;
		}
		
		return doSelect(invokers, url, invocation);
	}

	protected abstract <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation);

	/**
	 * Get the weight of the invoker's invocation which takes warmup time into
	 * account if the uptime is within the warmup time, the weight will be reduce
	 * proportionally
	 *
	 * @param invoker
	 *            the invoker
	 * @param invocation
	 *            the invocation of this invoker
	 * @return weight
	 */
	protected int getWeight(Invoker<?> invoker, Invocation invocation) {
		int weight = invoker.getUrl().getMethodParameter(invocation.getMethodName(), Constants.WEIGHT_KEY,
				Constants.DEFAULT_WEIGHT);
		if (weight > 0) {
			long timestamp = invoker.getUrl().getParameter(Constants.REMOTE_TIMESTAMP_KEY, 0L);
			if (timestamp > 0L) {
				int uptime = (int) (System.currentTimeMillis() - timestamp);
				int warmup = invoker.getUrl().getParameter(Constants.WARMUP_KEY, Constants.DEFAULT_WARMUP);
				if (uptime > 0 && uptime < warmup) {
					weight = calculateWarmupWeight(uptime, warmup, weight);
				}
			}
		}
		return weight >= 0 ? weight : 0;
	}
}

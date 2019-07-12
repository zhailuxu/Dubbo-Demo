package com.books.dubbo.demo.broadcast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.dubbo.common.json.JSON;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.NamedThreadFactory;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.RpcResult;
import org.apache.dubbo.rpc.cluster.Directory;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import org.apache.dubbo.rpc.cluster.support.AbstractClusterInvoker;


public class BroadcastClusterInvoker<T> extends AbstractClusterInvoker<T> {

	private static final Logger logger = LoggerFactory.getLogger(BroadcastClusterInvoker.class);
	private ThreadPoolExecutor paramCallPool = null;

	public BroadcastClusterInvoker(Directory<T> directory) {
		super(directory);

		int poolSize = directory.getUrl().getParameter("broadCastPoolSize", 8);
		paramCallPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(poolSize,
				new NamedThreadFactory("PARA-CALL-POOL", true));
	}

	public void close() {
		try {
			if (paramCallPool instanceof ExecutorService) {
				((ExecutorService) paramCallPool).shutdown();
			}
		} catch (Throwable t) {
			logger.warn("fail to destroy thread pool of server: " + t.getMessage(), t);
		}
	}

	

	
	public Result doInvokeParaCountDown(final Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadbalance)
			throws RpcException {
		Map<String, Object> allResult = new ConcurrentHashMap<String, Object>();
		int machineNum = invokers.size();
		CountDownLatch countDownLatch = new CountDownLatch(machineNum);
		for (Invoker<T> invoker : invokers) {
			try {
				paramCallPool.execute(new Runnable() {

					@Override
					public void run() {
						try {
							Result result = invoker.invoke(invocation);
							String url = invoker.getUrl().getAddress();
							allResult.put(url, result.getResult());

						} catch (RpcException e) {
							logger.warn(e.getMessage(), e);
						} catch (Throwable ee) {
							logger.warn(ee.getMessage(), ee);
						}finally {
							countDownLatch.countDown();
						}

					}
				});

			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}

		}
		// 等所有的完成
		try {
			countDownLatch.await(5000,TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("wait sub thread over error:" + e.getLocalizedMessage());
		}
	


		Map finalResult = new HashMap<String, Result>();
		finalResult.put("machineNum", machineNum);
		finalResult.put("result", allResult);


		Result result ;
		try {
			result = new RpcResult(JSON.json(finalResult));
		} catch (IOException e) {
			e.printStackTrace();
			result = new RpcResult(e);
		}

		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Result doInvoke(final Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadbalance)
			throws RpcException {
		checkInvokers(invokers, invocation);
		RpcContext.getContext().setInvokers((List) invokers);

		return doInvokeParaCountDown(invocation, invokers, loadbalance);
	}

}

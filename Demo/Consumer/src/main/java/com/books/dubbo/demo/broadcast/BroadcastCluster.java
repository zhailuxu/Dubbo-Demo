package com.books.dubbo.demo.broadcast;

import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.Cluster;
import org.apache.dubbo.rpc.cluster.Directory;

public class BroadcastCluster implements Cluster {

	public <T> Invoker<T> join(Directory<T> directory) throws RpcException {
		return new BroadcastClusterInvoker<T>(directory);
	}

}
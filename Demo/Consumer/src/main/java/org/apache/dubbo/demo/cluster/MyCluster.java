package org.apache.dubbo.demo.cluster;

import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.Cluster;
import org.apache.dubbo.rpc.cluster.Directory;

public class MyCluster implements Cluster{

    @Override
    public <T> Invoker<T> join(Directory<T> directory) throws RpcException {
        return new MyClusterInvoker(directory);
    }
}

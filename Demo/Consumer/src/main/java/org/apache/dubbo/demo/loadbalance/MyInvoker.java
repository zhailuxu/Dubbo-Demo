package org.apache.dubbo.demo.loadbalance;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

public class MyInvoker<T> implements org.apache.dubbo.rpc.Invoker<T> {

	public String getProviderId() {
		return providerId;
	}



	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	private String providerId;
	private URL url;

	public MyInvoker(String providerId, URL url) {
		this.url = url;
		this.providerId = providerId;
	}

	

	@Override
	public URL getUrl() {
		// TODO Auto-generated method stub
		return this.url;
	}

	@Override
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public Class<T> getInterface() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result invoke(Invocation invocation) throws RpcException {
		// TODO Auto-generated method stub
		return null;
	}

}

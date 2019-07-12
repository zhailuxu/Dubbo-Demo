package com.books.dubbo.demo.provider.mydispatcher;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.remoting.ChannelHandler;
import org.apache.dubbo.remoting.Dispatcher;
import org.apache.dubbo.remoting.transport.dispatcher.all.AllChannelHandler;

public class MyDispatcher implements Dispatcher {

    public static final String NAME = "mydispatcher";

    @Override
    public ChannelHandler dispatch(ChannelHandler handler, URL url) {
        return new AllChannelHandler(handler, url);
    }

}
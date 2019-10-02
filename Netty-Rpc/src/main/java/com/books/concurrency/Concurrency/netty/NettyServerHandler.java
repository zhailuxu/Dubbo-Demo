/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.books.concurrency.Concurrency.netty;

import io.netty.channel.ChannelHandler.Sharable;


import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handler implementation for the echo server.
 */
@Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

	// 根据消息内容和请求id，拼接消息帧
	public String generatorFrame(String msg, String reqId) {
		return msg + ":" + reqId + "|";
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {

		// 异步，释放IO线程
		AllChannelHandler.channelRead(() -> {
			try {
				System.out.println(msg);
				// 1.获取消息体，并且解析出请求id
				String str = (String) msg;
				String reqId = str.split(":")[1];

				// 2.拼接结果，请求id,协议帧分隔符(模拟服务端执行服务产生结果)
				String resp =  generatorFrame("im jiaduo ", reqId);

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				// 3.写回结果
				ctx.channel().writeAndFlush(Unpooled.copiedBuffer(resp.getBytes()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("------in active----");
		ctx.fireChannelInactive();
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("------handlerRemoved----");
		super.handlerRemoved(ctx);

	}

}

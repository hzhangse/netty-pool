package com.train.client.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.zhang.client.NettyTCPClient;
import com.zhang.client.NettyTCPRequest;
import com.zhang.client.NettyTCPResponse;
import com.zhang.client.NettyTCPResponseFuture;

public class NettyTCPClientTest {
	final String hostName = "127.0.0.1";
	final int port = 8080;
	 NettyTCPClient client = null;
	
	@BeforeClass
	public void setUp() throws Exception {
	
		InetSocketAddress serverAddr = new InetSocketAddress(hostName, port);
		Map<String, Integer> maxPerRoute = new HashMap<String, Integer>();
		maxPerRoute.put(serverAddr.getHostName() + ":" + serverAddr.getPort(),
				2);

	    client = new NettyTCPClient.ConfigBuilder()
				.maxIdleTimeInMilliSecondes(20 * 1000).maxPerRoute(maxPerRoute)
				.connectTimeOutInMilliSecondes(30 * 1000).build();
		
	}

	@Test(invocationCount = 10, threadPoolSize = 2)
	public void testRequest() throws Exception {

		final String postContent = "QUERY TIME ORDER"
				+ System.getProperty("line.separator");// json format
		

		final NettyTCPRequest request = new NettyTCPRequest(hostName, port);
	

		request.content(postContent, Charset.forName("UTF-8"));

		NettyTCPResponseFuture responseFuture = client.sendRequest(request);
		NettyTCPResponse response = (NettyTCPResponse) responseFuture.get();
		//client.close();
		print(response);
	}

	private void print(NettyTCPResponse response) {
		System.out.println();
		System.out.println("CHUNKED CONTENT :");
		for (ByteBuf buf : response.getContents()) {
			System.out.print(buf.toString(CharsetUtil.UTF_8));
		}
	}
}


package com.zhang.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * 
 * @author ryan
 *
 */
public class NettyTCPRequest {


	private ByteBuf content;
	private String hostname;
	



	private int port;
	

	private static final Charset DEFAUT_CHARSET = Charset.forName("GBK");

    public  NettyTCPRequest(String hostname,int port){
    	this.hostname = hostname;
    	this.port = port;
    }

	public NettyTCPRequest content(ByteBuf content) {
		if (null == content) {
			throw new NullPointerException("content");
		}

		this.content = content;
		return this;
	}

	public NettyTCPRequest content(byte[] content) {
		if (null == content) {
			throw new NullPointerException("content");
		}
		this.content = Unpooled.copiedBuffer(content);
		return this;
	}

	public NettyTCPRequest content(String content, Charset charset) {
		if (null == content) {
			throw new NullPointerException("content");
		}
		charset = null == charset ? DEFAUT_CHARSET : charset;
		this.content = Unpooled.copiedBuffer(content, charset);
		return this;
	}
	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}


	public ByteBuf getContent() {
		return content;
	}
}

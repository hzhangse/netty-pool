
package com.zhang.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

import com.zhang.client.NettyHttpResponseFuture;
import com.zhang.client.NettyTCPResponseFuture;
/**
 * 
 * @author ryan
 *
 */
public class NettyTCPResponseFutureUtil {

    private static final AttributeKey<Object> DEFAULT_ATTRIBUTE       = AttributeKey
                                                                          .valueOf("nettyTcpResponse");

    private static final AttributeKey<Object> ROUTE_ATTRIBUTE         = AttributeKey
                                                                          .valueOf("route");

    private static final AttributeKey<Object> FORCE_CONNECT_ATTRIBUTE = AttributeKey
                                                                          .valueOf("forceConnect");

    public static void attributeForceConnect(Channel channel, boolean forceConnect) {
        if (forceConnect) {
            channel.attr(FORCE_CONNECT_ATTRIBUTE).set(true);
        }
    }

    public static void attributeResponse(Channel channel, NettyTCPResponseFuture responseFuture) {
        channel.attr(DEFAULT_ATTRIBUTE).set(responseFuture);
        responseFuture.setChannel(channel);
    }

    public static void attributeRoute(Channel channel, InetSocketAddress route) {
        channel.attr(ROUTE_ATTRIBUTE).set(route);
    }

    public static NettyTCPResponseFuture getResponse(Channel channel) {
        return (NettyTCPResponseFuture) channel.attr(DEFAULT_ATTRIBUTE).get();
    }

    public static InetSocketAddress getRoute(Channel channel) {
        return (InetSocketAddress) channel.attr(ROUTE_ATTRIBUTE).get();
    }

    public static boolean getForceConnect(Channel channel) {
        Object forceConnect = channel.attr(FORCE_CONNECT_ATTRIBUTE).get();
        if (null == forceConnect) {
            return false;
        }
        return true;
    }


    public static void setPendingResponse(Channel channel) {
        NettyTCPResponseFuture responseFuture = getResponse(channel);
        NettyTCPResponseBuilder responseBuilder = new NettyTCPResponseBuilder();
        responseBuilder.setSuccess(true);
        responseFuture.setResponseBuilder(responseBuilder);
    }


    public static void setPendingContent(Channel channel, ByteBuf tcpContent) {
    	NettyTCPResponseFuture responseFuture = getResponse(channel);
        NettyTCPResponseBuilder responseBuilder = responseFuture.getResponseBuilder();
        responseBuilder.addContent(tcpContent.retain());
    }

    public static boolean done(Channel channel) {
        NettyTCPResponseFuture responseFuture = getResponse(channel);
        if (null != responseFuture) {
            return responseFuture.done();
        }

        return true;
    }

    public static boolean cancel(Channel channel, Throwable cause) {
    	NettyTCPResponseFuture responseFuture = getResponse(channel);
        return responseFuture.cancel(cause);
    }
}

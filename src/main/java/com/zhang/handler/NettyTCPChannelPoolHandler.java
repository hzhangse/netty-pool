package com.zhang.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.zhang.pool.NettyTCPChannelPool;
import com.zhang.util.NettyHttpResponseFutureUtil;
import com.zhang.util.NettyTCPResponseFutureUtil;

/**
 * 
 * @author ryan
 *
 */
public class NettyTCPChannelPoolHandler extends
		SimpleChannelInboundHandler<Object> {
	private static final Logger logger = Logger
			.getLogger(NettyTCPChannelPoolHandler.class.getName());

	private NettyTCPChannelPool channelPool;

	/**
	 * @param channelPool
	 */
	public NettyTCPChannelPoolHandler(NettyTCPChannelPool channelPool) {
		super();
		this.channelPool = channelPool;
	}

	/**
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#userEventTriggered(io.netty.channel.ChannelHandlerContext,
	 *      java.lang.Object)
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
		if (evt instanceof IdleStateEvent) {
			logger.log(Level.WARNING, "remove idle channel: " + ctx.channel());
			ctx.channel().close();
		} else {
			ctx.fireUserEventTriggered(evt);
		}
	}

	/**
	 * @param channelPool
	 *            the channelPool to set
	 */
	public void setChannelPool(NettyTCPChannelPool channelPool) {
		this.channelPool = channelPool;
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
		ByteBuf buf = (ByteBuf) msg;
		NettyTCPResponseFutureUtil.setPendingResponse(ctx.channel());
		NettyTCPResponseFutureUtil.setPendingContent(ctx.channel(), buf);
		
//		byte[] req = new byte[buf.readableBytes()];
//		buf.readBytes(req);
//		String body = new String(req, "UTF-8");
//		System.out.println("msg received : " + body);
		NettyTCPResponseFutureUtil.done(ctx.channel());
		// the maxKeepAliveRequests config will cause server close the channel,
		// and return 'Connection: close' in headers

		channelPool.returnChannel(ctx.channel());

	}
}

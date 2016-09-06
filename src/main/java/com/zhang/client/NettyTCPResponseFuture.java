package com.zhang.client;

import io.netty.channel.Channel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.zhang.util.NettyTCPResponseBuilder;

/**
 * 
 * @author ryan
 *
 */
public class NettyTCPResponseFuture {
	private final CountDownLatch latch = new CountDownLatch(1);

	private volatile boolean isDone = false;

	private volatile boolean isCancel = false;

	private final AtomicBoolean isProcessed = new AtomicBoolean(false);

	private volatile NettyTCPResponseBuilder responseBuilder;

	private volatile Channel channel;

	public boolean cancel(Throwable cause) {
		if (isProcessed.getAndSet(true)) {
			return false;
		}

		responseBuilder = new NettyTCPResponseBuilder();
		responseBuilder.setSuccess(false);
		responseBuilder.setCause(cause);
		isCancel = true;
		latch.countDown();
		return true;
	}

	public NettyTCPResponse get() throws InterruptedException,
			ExecutionException {
		latch.await();
		return responseBuilder.build();
	}

	public NettyTCPResponse get(long timeout, TimeUnit unit)
			throws TimeoutException, InterruptedException {
		if (!latch.await(timeout, unit)) {
			throw new TimeoutException();
		}
		return responseBuilder.build();
	}

	public boolean done() {
		if (isProcessed.getAndSet(true)) {
			return false;
		}
		isDone = true;
		latch.countDown();
		return true;
	}

	public boolean isCancelled() {
		return isCancel;
	}

	public boolean isDone() {
		return isDone;
	}

	/**
	 * Getter method for property <tt>channel</tt>.
	 * 
	 * @return property value of channel
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * Setter method for property <tt>channel</tt>.
	 * 
	 * @param channel
	 *            value to be assigned to property channel
	 */
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	/**
	 * Getter method for property <tt>responseBuilder</tt>.
	 * 
	 * @return property value of responseBuilder
	 */
	public NettyTCPResponseBuilder getResponseBuilder() {
		return responseBuilder;
	}

	/**
	 * Setter method for property <tt>responseBuilder</tt>.
	 * 
	 * @param responseBuilder
	 *            value to be assigned to property responseBuilder
	 */
	public void setResponseBuilder(NettyTCPResponseBuilder responseBuilder) {
		this.responseBuilder = responseBuilder;
	}
}

package com.weicoder.socket.impl.netty;

import com.weicoder.socket.Session;
import com.weicoder.socket.process.Process;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * Netty 处理器
 * @author WD
 */
@Sharable
public final class NettyHandler extends SimpleChannelInboundHandler<ByteBuf> {
	// 名称
	private String	name;
	// 消息处理器
	private Process	process;

	/**
	 * 构造
	 * @param name 名称
	 * @param process 处理器
	 */
	public NettyHandler(String name, Process process) {
		this.name = name;
		this.process = process;
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		process.closed(getSesson(ctx.channel()));
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		process.connected(getSesson(ctx.channel()));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		// 声明字节流
		byte[] data = new byte[msg.readableBytes()];
		// 读取字节流
		msg.readBytes(data);
		// 交给数据处理器
		process.process(getSesson(ctx.channel()), data);
	}

	/**
	 * 获得包装Session
	 * @param channel netty channel
	 * @return Session
	 */
	private Session getSesson(Channel channel) {
		// 获得SessionId
		int id = channel.hashCode();
		// 获得包装Session
		Session s = process.session(id);
		// 如果为null
		if (s == null) {
			// 实例化包装Session
			s = new NettySession(name, channel);
		}
		// 返回
		return s;
	}
}
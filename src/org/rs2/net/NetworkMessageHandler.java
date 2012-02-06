package org.rs2.net;

import java.util.concurrent.ExecutorService;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.rs2.model.World;
import org.rs2.model.player.GameSession;
import org.rs2.net.msg.Message;
import org.rs2.service.logic.LogicService;
import org.rs2.util.BandwidthMonitor;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class NetworkMessageHandler extends SimpleChannelHandler {
	
	/**
	 * js5 workers
	 */
	public ExecutorService js5Workers = null;
	
	/**
	 * Handles exceptions
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		//e.getCause().printStackTrace();
	}
	
	/**
	 * Invoked when a connection is opened
	 */
	@Override
	public final void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		
	}
	
	@Override
	public final void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if(e instanceof MessageEvent && ((MessageEvent) e).getMessage() instanceof Message) {
			Message buffer = (Message)((MessageEvent) e).getMessage();
			BandwidthMonitor.bytesSent.addAndGet(buffer.getBuffer().readableBytes());
		}
		super.handleDownstream(ctx, e);
	}
	
	@Override
	public final void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if(e instanceof MessageEvent && ((MessageEvent) e).getMessage() instanceof Message) {
			Message buffer = (Message)((MessageEvent) e).getMessage();
			BandwidthMonitor.bytesReceived.addAndGet(buffer.getBuffer().readableBytes());
		}
		super.handleUpstream(ctx, e);
	}

	/**
	 * Invoked when a connection is closed
	 */
	@Override
	public final void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		GameSession session = (GameSession)ctx.getAttachment();
		if(session != null) {
			synchronized(LogicService.getWorkerThread()) {
				World.getSingleton().unregisterPlayer(session.getPlayer());
			}
		}
		ctx.getChannel().close();
		
	}

	/**
	 * Invoked when a packet is received
	 */
	@Override
	public final void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {	
		GameSession session = (GameSession)ctx.getAttachment();
		if(e.getMessage() instanceof Message) {
			if(session != null) {
				synchronized(LogicService.getWorkerThread()) {
					Message packet = (Message)e.getMessage();
					session.pushPacket(packet);
				}
			}
		}
		if(e.getMessage() instanceof ChannelBuffer) {
			ctx.getChannel().write(ctx);
		}
	}

}
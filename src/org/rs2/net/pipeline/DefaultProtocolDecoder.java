package org.rs2.net.pipeline;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.rs2.model.player.GameSession;
import org.rs2.net.Constants;
import org.rs2.net.msg.Message;
import org.rs2.net.msg.Message.PacketType;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class DefaultProtocolDecoder extends FrameDecoder {
	
	/**
	 * The game session this decoder is for
	 */
	private GameSession session;
	
	/**
	 * Construct the instance
	 * @param session
	 */
	public DefaultProtocolDecoder(GameSession session) {
		setSession(session);
		getSession().getChannel().getPipeline().getContext("handler").setAttachment(getSession());
	}

	/**
	 * Decodes the packet
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if(buffer.readable()) {
			int opcode = buffer.readUnsignedByte();
			int length = Constants.PACKET_LENGTHS[opcode];
			if (opcode < 0) {
				buffer.discardReadBytes();
				return null;
			}
			if (length == -1) {
				if(buffer.readable()) {
					length = buffer.readUnsignedByte();
				}
			}
			if(length <= buffer.readableBytes() && length > 0) {
				byte[] payload = new byte[length];
				buffer.readBytes(payload, 0, length);
				Message message = new Message(opcode, PacketType.STANDARD, ChannelBuffers.wrappedBuffer(payload));
				return message;
			}
		}
		return null;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(GameSession session) {
		this.session = session;
	}

	/**
	 * @return the session
	 */
	public GameSession getSession() {
		return session;
	}

}

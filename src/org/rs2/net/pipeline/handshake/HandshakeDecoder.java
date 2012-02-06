package org.rs2.net.pipeline.handshake;

import java.security.SecureRandom;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.rs2.net.pipeline.js5.JS5Decoder;
import org.rs2.net.pipeline.login.LoginDecoder;
import org.rs2.util.Configuration;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class HandshakeDecoder extends FrameDecoder {
	
	/**
	 * Request lists
	 */
	private static final int JS5_PROTOCOL_REQUEST = 15;
	private static final int LOGIN_PROTOCOL_REQUEST = 14;
	private static final int ACCOUNT_CREATION_PROTOCOL_REQUEST_1 = 85;
	private static final int ACCOUNT_CREATION_PROTOCOL_REQUEST_2  = 118;
	private static final int ACCOUNT_CREATION_PROTOCOL_REQUEST_3 = 48;
	

	@Override
	protected Object decode(ChannelHandlerContext context, Channel channel, ChannelBuffer buffer) throws Exception {
		if(context.getPipeline().get(HandshakeDecoder.class) != null) {
			context.getPipeline().remove(this);
		}
		byte serviceReq = (byte) (buffer.readByte() & 0xFF);
		ChannelBuffer msg = ChannelBuffers.dynamicBuffer();
		switch(serviceReq) {
		case JS5_PROTOCOL_REQUEST:
			int revision = buffer.readInt();
			if(revision != Configuration.REVISION) {
				msg.writeByte(6);
			} else {
				msg.writeByte(0);
				context.getPipeline().addBefore("encoder", "decoder", new JS5Decoder());
			}
			channel.write(msg);
			break;
		case LOGIN_PROTOCOL_REQUEST:
			if(1 <= buffer.readableBytes()) {
				buffer.readUnsignedByte();
				msg.writeByte(0);
				msg.writeLong(new SecureRandom().nextLong());
				context.getPipeline().addBefore("encoder", "decoder", new LoginDecoder());
				channel.write(msg);
			}
			break;
		}
		return null;
	}

}
package org.rs2.net.pipeline.login;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;
import org.rs2.service.login.LoginService;
import org.rs2.service.login.LoginServiceTask;
import org.rs2.util.Configuration;
import org.rs2.util.Misc;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class LoginDecoder extends ReplayingDecoder<LoginStage> {

	/**
	 * Construct the decoder
	 */
	public LoginDecoder() {
		checkpoint(LoginStage.SERVICE_CHECK);
	}
	
	/**
	 * Variables
	 */
	private short loginBlockExpectedSize = -1;
	
	/**
	 * Decode logins
	 */
	@SuppressWarnings("unused")
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel chan,ChannelBuffer in, LoginStage stage) throws Exception {
		switch(stage) {
		case SERVICE_CHECK:
			if(3 <= in.readableBytes()) {
				in.readUnsignedByte();
				loginBlockExpectedSize = (short) in.readUnsignedShort();
				checkpoint(LoginStage.LOGIN_ATTEMPT);
			} else {
				System.out.println("Failure?!");
			}
			break;
		case LOGIN_ATTEMPT:
			if(loginBlockExpectedSize <= in.readableBytes()) {
				int loginActualSize = loginBlockExpectedSize - (36 + 1 + 1 + 2);
				if(loginActualSize < 0) {
					chan.close();
					return null;
				}
				int revision = in.readInt();
				if(revision != Configuration.REVISION) {
					chan.close();
					return null;
				}
				int lowMem = in.readUnsignedByte();
				in.readInt();
				for(int i = 0; i < 24; i++) {
					in.readByte();
				}
				Misc.readRS2String(in);
				for(int i = 0; i < 29; i++) {
					in.readInt();
				}
				int encryptPacketSizeNow = in.readUnsignedByte();
				boolean isHD = true;
				if(encryptPacketSizeNow != 10) {
					in.readUnsignedByte();
					isHD = false;
				}
				long clientKey = in.readLong();
				long serverKey = in.readLong();
				long nameInLong = in.readLong();
				
				String username = Misc.longToPlayerName(nameInLong);
				String password = Misc.readRS2String(in);
				
				LoginService.getSingleton().submit(new LoginServiceTask(username, password, chan, ctx));
			}
			break;
 		}
		return null;
	}

}
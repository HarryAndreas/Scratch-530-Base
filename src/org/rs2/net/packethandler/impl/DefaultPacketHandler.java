package org.rs2.net.packethandler.impl;

import org.rs2.model.player.GameSession;
import org.rs2.net.msg.Message;
import org.rs2.net.packethandler.PacketHandler;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class DefaultPacketHandler implements PacketHandler {

	@Override
	public int[] getBinds() {
		return new int[] {
			59 // Mouse click
		};
	}

	@Override
	public void handlePacket(Message m, GameSession gs) {
		return;
	}

}
package org.rs2.net.packethandler.impl;

import org.rs2.model.player.GameSession;
import org.rs2.model.player.Player;
import org.rs2.net.msg.Message;
import org.rs2.net.packethandler.PacketHandler;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class WalkingPacketHandler implements PacketHandler {

	@Override
	public int[] getBinds() {
		return new int[] { 
			49, 119, 138	
		};
	}

	@Override
	public void handlePacket(Message m, GameSession gs) {
		Player p = gs.getPlayer();
		int size = m.getLength();
		if(m.getOpcode() == 119) {
			size -= 14;
		}
		if(m.getOpcode() != 138) {
			gs.getPlayer().setDistancedTask(null);
		}
		gs.getPlayer().getMovement().reset();
		int steps = (size - 5) / 2;
		int[] pathX = new int[steps];
		int[] pathY = new int[steps];
		// writeWordBigEndianA = writeLEShortA
		int firstX = m.readLEShortA() - (gs.getPlayer().getLocation().getRegionX() - 6) * 8;
		int firstY = m.readShortA() - (gs.getPlayer().getLocation().getRegionY() - 6) * 8;
		m.readByteC();
		for(int i = 0; i < steps; i ++) {
			pathX[i] = m.readByte();
			pathY[i] = m.readByteS();
		}
		p.getMovement().addToWalkingQueue(firstX, firstY);
		for(int i = 0; i < steps; i++) {
			pathX[i] += firstX;
			pathY[i] += firstY;
			p.getMovement().addToWalkingQueue(pathX[i], pathY[i]);
		}
		
	}

}
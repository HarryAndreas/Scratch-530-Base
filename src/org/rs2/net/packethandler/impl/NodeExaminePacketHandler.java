package org.rs2.net.packethandler.impl;

import org.rs2.model.item.ItemDefinitionsManager;
import org.rs2.model.player.GameSession;
import org.rs2.net.ActionSender;
import org.rs2.net.msg.Message;
import org.rs2.net.packethandler.PacketHandler;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class NodeExaminePacketHandler implements PacketHandler {

	@Override
	public int[] getBinds() {
		return new int[] { 38, 88, 84 };
	}

	@Override
	public void handlePacket(Message m, GameSession gs) {
		String examine = "null";
		switch(m.getOpcode()) {
		case 38:
			examine = ItemDefinitionsManager.getSingleton().getDefinition(m.readLEShortA()).getExamine();
			break;
		}
		ActionSender.sendMessage(gs.getPlayer(), examine);
	}

}

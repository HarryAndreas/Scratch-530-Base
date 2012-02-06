package org.rs2.net.packethandler.impl;

import org.rs2.model.content.MagicButtonHandler;
import org.rs2.model.player.GameSession;
import org.rs2.model.player.Levels;
import org.rs2.net.ActionSender;
import org.rs2.net.msg.Message;
import org.rs2.net.packethandler.PacketHandler;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class ActionButtonPacketHandler implements PacketHandler {

	@Override
	public int[] getBinds() {
		return new int[] {
			113, 233, 21, 169,232, 214, 173, 90, 133
		};
	}

	@Override
	public void handlePacket(Message m, GameSession gs) {
		int interfaceID = m.readShort() & 0xFFFF;
		int childID = m.readShort() & 0xFFFF;
		switch(interfaceID) {
			case 320:
				int skill = -1;
				for(int[] i : Levels.SKILL_TAB_BUTTONS) {
					if(i[0] == childID) {
						skill = i[1];
						break;
					}
				}
				if(skill != -1) {
					gs.getPlayer().addAttribute("skillGuide", skill);
					ActionSender.sendConfig(gs.getPlayer(), 965, skill);
					ActionSender.sendInterface(gs.getPlayer(), 0, 548, 8, 499);
				}
				break;
			case 499:
				skill = gs.getPlayer().getAttribute("skillGuide", -1);
				if(skill != -1) {
					switch (childID) {
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
					case 16:
					case 17:
					case 18:
					case 19:
					case 20:
					case 21:
					case 22:
					case 23:
						ActionSender.sendConfig(gs.getPlayer(), 965, (1024 * (childID - 10)) + skill);
						break;
					}
				}
			break;
			case 750:
			case 261:
				if(childID == 1) {
					gs.getPlayer().getMovement().toggleRun();
				}
				break;
			case 182:
				ActionSender.sendLogoutPacket(gs.getPlayer());
			break;
			case 192:
				MagicButtonHandler.handleButton(gs.getPlayer(), interfaceID, childID);
				break;
		}
	}

}
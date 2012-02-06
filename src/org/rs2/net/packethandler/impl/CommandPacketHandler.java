package org.rs2.net.packethandler.impl;

import org.rs2.model.World;
import org.rs2.model.item.Item;
import org.rs2.model.mask.Animation;
import org.rs2.model.mask.UpdateFlag;
import org.rs2.model.player.GameSession;
import org.rs2.net.ActionSender;
import org.rs2.net.msg.Message;
import org.rs2.net.packethandler.PacketHandler;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class CommandPacketHandler implements PacketHandler {

	@Override
	public int[] getBinds() {
		return new int[] { 107 };
	}

	@Override
	public void handlePacket(Message m, final GameSession gs) {
		String command = m.readRS2String();
		String[] split = command.split(" ");
		if(split[0].equalsIgnoreCase("item")) {
			int id = Integer.parseInt(split[1]);
			int am = Integer.parseInt(split[2]);
			gs.getPlayer().getInventory().add(new Item(id, am));
			gs.getPlayer().getInventory().refresh();
		}
		if(split[0].equalsIgnoreCase("anim")) {
			int id = Integer.parseInt(split[1]);
			gs.getPlayer().getUpdateFlags().setCurrentAnimation(Animation.createAnimation(id));
			gs.getPlayer().getUpdateFlags().flag(UpdateFlag.ANIMATION);
		}
		if(split[0].equalsIgnoreCase("players")) {
			ActionSender.sendMessage(gs.getPlayer(), "There are currently "+World.getSingleton().getRegisteredPlayers().size()+" players online.");
		}
		if(split[0].equalsIgnoreCase("pos")) {
			ActionSender.sendMessage(gs.getPlayer(), "Location: "+gs.getPlayer().getLocation().getX()+" "+gs.getPlayer().getLocation().getY());
		}
	}

}
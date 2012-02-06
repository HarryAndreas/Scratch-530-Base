package org.rs2.task.impl;

import org.rs2.model.player.Player;
import org.rs2.net.msg.Message;
import org.rs2.net.packethandler.PacketManager;
import org.rs2.task.Task;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class PlayerPacketProcessTask extends Task {

	/**
	 * Constructor
	 * @param args
	 */
	public PlayerPacketProcessTask(Object... args) {
		super(args);
	}
	
	/**
	 * Executes the task
	 */
	@Override
	public void execute() {
		Player p = (Player)getArgs()[0];
		Message msg = null;
		while((msg = p.getSession().getQueuedPackets().poll()) != null) {
			PacketManager.getSingleton().handlePacket(p.getSession(), msg);
		}
	}

}
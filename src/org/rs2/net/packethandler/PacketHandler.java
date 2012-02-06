package org.rs2.net.packethandler;

import org.rs2.model.player.GameSession;
import org.rs2.net.msg.Message;

/**
 * 508 Base
 * @author Harry Andreas
 */
public interface PacketHandler {
	
	/**
	 * Gets the opcodes to bind
	 * @return
	 */
	public int[] getBinds();
	
	/**
	 * Handles the packet
	 * @param m The packet 
	 * @param s The session
	 */
	public void handlePacket(Message m, GameSession gs);
	
}
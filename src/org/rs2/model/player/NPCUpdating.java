package org.rs2.model.player;

import java.util.Iterator;

import org.rs2.model.World;
import org.rs2.model.npc.NPC;
import org.rs2.net.msg.Message;
import org.rs2.net.msg.Message.PacketType;
import org.rs2.net.msg.MessageBuilder;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class NPCUpdating {
	
	/**
	 * Constructs an npc updating packet
	 * @param p
	 * @return
	 */
	public static Message update(Player p) {
		MessageBuilder packet = new MessageBuilder(222, PacketType.VAR_SHORT);
		MessageBuilder block = new MessageBuilder();
		packet.startBitAccess();
		packet.writeBits(8, p.getRegionalNPCs().size());//list size
		Iterator<NPC> it$ = p.getRegionalNPCs().iterator();
		while(it$.hasNext()) {
			NPC n = it$.next();
			if(n == null || !World.getSingleton().getRegisteredNPCs().contains(n) || !n.getLocation().withinDistance(p.getLocation()) || n.getUpdateFlags().isDidTeleport()) {
				packet.writeBits(1, 1);
				packet.writeBits(2, 3);
				it$.remove();
			} else {
				appendMovement(packet, n);
				if(n.getUpdateFlags().updateRequired()) {
					appendBlock(block, n);
				}
			}
		}
		for(NPC n : World.getSingleton().getRegisteredNPCs()) {
			if(n == null || p.getRegionalNPCs().contains(n) || !n.getLocation().withinDistance(p.getLocation())) {
				continue;
			}
			p.getRegionalNPCs().add(n);
			addNPC(packet, n, p);
			if(n.getUpdateFlags().updateRequired()) {
				appendBlock(block, n);
			}
		}
		if(block.position() >= 3) {
			packet.writeBits(15, 32767);
		}
		packet.finishBitAccess();
		if(block.position() > 0) {
			packet.writeBytes(block.getBuffer());
		}
		return packet.toMessage();
	}
	
	/**
	 * Appends the update block to the packet builder
	 * @param block The packet builder 
	 * @param n The npc for masks
	 */
	private static void appendBlock(MessageBuilder block, NPC n){
		int mask = 0x0;
		block.writeByte(mask);
	}
	
	/**
	 * Applies movement for the npc
	 * @param packet The packet
	 * @param n The npc
	 */
	private static void appendMovement(MessageBuilder packet, NPC n) {
		int spr = n.getMovement().getWalkingSprite();
		if(spr != -1) {
			packet.writeBits(1, 1);
			packet.writeBits(2, 1);
			packet.writeBits(3, DIRECTION_KEYS[spr]);
			packet.writeBits(1, n.getUpdateFlags().updateRequired() ? 1: 0);
		} else {
			if(!n.getUpdateFlags().updateRequired()) {
				packet.writeBits(1, 0);
			} else {
				packet.writeBits(1, 1);
				packet.writeBits(2, 0);
			}
		}
	}
	
	/**
	 * Direction keys for movement
	 */
	private static final byte[] DIRECTION_KEYS = new byte[] { 1, 2, 4, 7, 6, 5, 3, 0 };
	
	/**
	 * Add's an npc to the packet
	 * @param packet
	 */
	private static void addNPC(MessageBuilder packet, NPC n, Player p) {
		packet.writeBits(15 , n.getIndex());
		packet.writeBits(14, n.getId());
		packet.writeBits(1, n.getUpdateFlags().updateRequired() ? 1 : 0);
		int y = n.getLocation().getY() - p.getLocation().getY();
		if(y < 0) {
			y += 32;
		}
		int x = n.getLocation().getX() - p.getLocation().getX();
		if(x < 0) {
			x += 32;
		}
		packet.writeBits(5, y);
		packet.writeBits(5, x);
		packet.writeBits(3, n.getLocation().getZ());
		packet.writeBits(1, 1);
	}

}
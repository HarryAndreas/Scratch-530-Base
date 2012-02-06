package org.rs2.model.player;

import java.util.Iterator;

import org.rs2.model.World;
import org.rs2.model.item.Item;
import org.rs2.model.mask.ChatMessage;
import org.rs2.model.mask.UpdateFlag;
import org.rs2.model.mask.UpdateFlags;
import org.rs2.model.player.containers.Equipment;
import org.rs2.net.ActionSender;
import org.rs2.net.msg.Message.PacketType;
import org.rs2.net.msg.MessageBuilder;
import org.rs2.net.packethandler.impl.PlayerChatPacketHandler;
import org.rs2.util.Misc;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class PlayerUpdating {
	
	/**
	 * Updates the player
	 * @param p
	 */
	public static void updatePlayer(Player p) {
		if(p.getMovement().isMapRegionDidChange()) {
			ActionSender.sendMapRegion(p);
		}
		MessageBuilder packet = new MessageBuilder(216, PacketType.VAR_SHORT);
		MessageBuilder block = new MessageBuilder();
		packet.startBitAccess();
		applyThisMovement(p, packet);
		if(!p.getUpdateFlags().getUpdateFlags().isEmpty()) {
			applyBlock(p, block, false);
		}
		packet.writeBits(8, p.getRegionalPlayers().size());
		Iterator<Player> it$ = p.getRegionalPlayers().iterator();
		while(it$.hasNext()) {
			Player regional = it$.next();
			if((World.getSingleton().getRegisteredPlayers().get(regional.getIndex()) == null) || !regional.getLocation().withinDistance(p.getLocation()) || regional.getUpdateFlags().isDidTeleport()) {
				packet.writeBits(1, 1);
				packet.writeBits(2, 3);
				it$.remove();
				continue;
			}
			applyPlayerMovement(regional, packet);
			if(!regional.getUpdateFlags().getUpdateFlags().isEmpty()) {
				applyBlock(regional, block, false);
			}
		}
		int addedAmount = 0;
		for(Player wp : World.getSingleton().getRegisteredPlayers()) {
			if(addedAmount >= 25 || p.getRegionalPlayers().size() >= 255) {
				break;
			}
			if(p.getRegionalPlayers().contains(wp) || wp == p) {
				continue;
			}
			if(wp.getLocation().withinDistance(p.getLocation())) {
				addedAmount++;
				p.getRegionalPlayers().add(wp);
				addPlayer(p, wp, packet);
				applyBlock(wp, block, true);
			}
		}
		if(block.position() > 0) {
			packet.writeBits(11, 2047);
		}
		packet.finishBitAccess();
		if(block.position() > 0) {
			packet.writeBytes(block.getBuffer());
		}
		p.getSession().getChannel().write(packet.toMessage());
	}
	
	/**
	 * Adds a player to the packet
	 * @param wp
	 * @param block
	 */
	private static void addPlayer(Player p, Player wp, MessageBuilder block) {
		block.writeBits(11, wp.getIndex());
		int yPos = wp.getLocation().getY() - p.getLocation().getY();
		int xPos = wp.getLocation().getX() - p.getLocation().getX();
		if(xPos < 0) {
			xPos += 32;
		}
		if(yPos < 0) {
			yPos += 32;
		}
		block.writeBits(5, xPos);
		block.writeBits(1, 1);
		block.writeBits(3, 1);
		block.writeBits(1, 1);
		block.writeBits(5, yPos);
	}
	
	/**
	 * 
	 * @param p
	 * @param packet
	 */
	private static void applyPlayerMovement(Player p, MessageBuilder packet) {
		if(p.getMovement().getWalkingSprite() == -1) {
			if(p.getUpdateFlags().updateRequired()) {
				packet.writeBits(1, 1);
				packet.writeBits(2, 0);
			} else {
				packet.writeBits(1, 0);
			}
		} else if(p.getMovement().getRunningSprite() == -1) {
			packet.writeBits(1, 1);
			packet.writeBits(2, 1);
			packet.writeBits(3, p.getMovement().getWalkingSprite());
			packet.writeBits(1, p.getUpdateFlags().updateRequired() ? 1 : 0);
		} else {
			packet.writeBits(1, 1);
			packet.writeBits(2, 2);
			packet.writeBits(3, p.getMovement().getWalkingSprite());
			packet.writeBits(3, p.getMovement().getRunningSprite());
			packet.writeBits(1, p.getUpdateFlags().updateRequired() ? 1 : 0);
		
		}
	}
	
	/**
	 * Applys the movement packet to the player
	 * @param p The player
	 * @param packet The packet to add to
	 */
	private static void applyThisMovement(Player p, MessageBuilder packet) {
		boolean updateReq =  !p.getUpdateFlags().getUpdateFlags().isEmpty();
		if(p.getUpdateFlags().isDidTeleport()) {
			packet.writeBits(1, 1);
			packet.writeBits(2, 3);
			packet.writeBits(7, p.getLocation().getLocalX(p.getUpdateFlags().getLastKnownLocation()));
			packet.writeBits(1, 1);
			packet.writeBits(2, p.getLocation().getZ());
			packet.writeBits(1, updateReq ? 1 : 0);
			packet.writeBits(7, p.getLocation().getLocalX(p.getUpdateFlags().getLastKnownLocation()));
		} else {
			if(p.getMovement().getWalkingSprite() == -1) {
				packet.writeBits(1, updateReq ? 1 : 0);
				if(updateReq) {
					packet.writeBits(2, 0);
				}
			} else {
				if(p.getMovement().getRunningSprite() != -1) {
					packet.writeBits(1, 1);
					packet.writeBits(2, 2);
					packet.writeBits(3, p.getMovement().getWalkingSprite());
					packet.writeBits(3, p.getMovement().getRunningSprite());
					packet.writeBits(1, updateReq ? 1 : 0);
				} else {
					packet.writeBits(1, 1);
					packet.writeBits(2, 1);
					packet.writeBits(3, p.getMovement().getWalkingSprite());
					packet.writeBits(1, updateReq ? 1 : 0);
				}
			}
		}
	}
	
	/**
	 * Applys the update block
	 * @param p The player
	 * @param block The packet builder
	 */
	private static void applyBlock(Player p, MessageBuilder block, boolean forceApp) {
		int mask = 0x0;
		UpdateFlags f = p.getUpdateFlags();
		if(f.isFlagged(UpdateFlag.CHAT)) {
			mask |= 0x8;
		}
		if(f.isFlagged(UpdateFlag.ANIMATION)) {
			mask |= 0x1;
		}
		if(f.isFlagged(UpdateFlag.APPERANCE) || forceApp) {
			mask |= 0x80;
		}
		if(mask >= 0x100) {
			mask |= 0x10;
			block.writeByte((byte) (mask & 0xFF));
			block.writeByte((byte) (mask >> 8));
		} else {
			block.writeByte((byte) (mask & 0xFF));
		}
		if(f.isFlagged(UpdateFlag.CHAT)) {
			applyChat(p, block);
		}
		if(f.isFlagged(UpdateFlag.ANIMATION)) {
			applyAnimation(p, block);
		}
		if(f.isFlagged(UpdateFlag.APPERANCE) || forceApp) {
			applyAppearance(p, block);
		}
	}
	
	/**
	 * Applys the chat mask to the block
	 * @param p The player
	 * @param block The message builder
	 */
	private static void applyChat(Player p, MessageBuilder block) {
		ChatMessage msg = p.getUpdateFlags().getCurrentChat();
		block.writeShortA(msg.getEffect());
		block.writeByteC(p.getRights());
		byte[] chatString = new byte[256];
		chatString[0] = (byte) msg.getChatMsg().length();
		int offset = 1 + PlayerChatPacketHandler.encryptPlayerChat(chatString, 0, 1, msg.getChatMsg().length(), msg.getChatMsg().getBytes());
		block.writeByteC(offset);
		block.writeBytes(chatString, 0, offset);
	}
	
	/**
	 * Applys the animation
	 * @param p Player
	 * @param block Block code
	 */
	private static void applyAnimation(Player p, MessageBuilder block) {
		block.writeShort(p.getUpdateFlags().getCurrentAnimation().getId());
		block.writeByteS(p.getUpdateFlags().getCurrentAnimation().getDelay());
	}
	
	/**
	 * Applys the appearance block
	 * @param p The player
	 * @param block The update block builder
	 */
	private static void applyAppearance(Player p, MessageBuilder block) {
		Looks l = p.getLooks();
		MessageBuilder bldr = new MessageBuilder();
		bldr.writeByte((byte) (l.getGender() & 0xFF));
		if((l.getGender() & 0x2) == 2) {
			bldr.writeByte((byte) 0);
			bldr.writeByte((byte) 0);
		}
		bldr.writeByte(-1);
		bldr.writeByte(-1);
		if(l.isAsNpc()) {
			bldr.writeShort(-1);
			bldr.writeShort(l.getNpcId());
		} else {
			Item[] e = p.getEquipment().getContainer().getItems();
			for(int i = 0; i < 4; i++) {
				if(e[i] != null) {
					bldr.writeShort(32768 + e[i].getDefinition().getWieldId());
				} else {
					bldr.writeByte(0);
				}
			} 
			if(e[Equipment.SLOT_CHEST] != null) {
				bldr.writeShort(32768 + e[Equipment.SLOT_CHEST].getDefinition().getWieldId());
			} else {
				bldr.writeShort(0x100 + l.getLooks()[2]);
			}
			if(e[Equipment.SLOT_SHIELD] != null) {
				bldr.writeShort(32768 + e[Equipment.SLOT_SHIELD].getDefinition().getWieldId());
			} else {
				bldr.writeByte(0);
			}
			Item chest = e[Equipment.SLOT_CHEST];
			if(chest != null) {
				if(!Equipment.isFullBody(chest.getDefinition())) {
					bldr.writeShort(0x100 + l.getLooks()[3]);
				} else {
					bldr.writeByte(0);
				}
			} else {
				bldr.writeShort(0x100 + l.getLooks()[3]);
			}
			Item legs = e[Equipment.SLOT_LEGS];
			if(legs != null) {
				bldr.writeShort(32768 + legs.getDefinition().getWieldId());
			} else {
				bldr.writeShort(0x100 + l.getLooks()[5]);
			}
			Item helm = e[Equipment.SLOT_HAT];
			if(helm != null) {
				if(!Equipment.isFullHat(helm.getDefinition()) || !Equipment.isFullMask(helm.getDefinition())) {
					bldr.writeShort(0x100 + l.getLooks()[0]);
				} else {
					bldr.writeByte(0);
				}
			} else {
				bldr.writeShort(0x100 + l.getLooks()[0]);
			}
			Item gloves = e[Equipment.SLOT_HANDS];
			if(gloves != null) {
				bldr.writeShort(32768 + gloves.getDefinition().getWieldId());
			} else {
				bldr.writeShort(0x100 + l.getLooks()[4]);
			}
			Item boots = e[Equipment.SLOT_FEET];
			if(boots != null) {
				bldr.writeShort(32768 + boots.getDefinition().getWieldId());
			} else {
				bldr.writeShort(0x100 + l.getLooks()[6]);
			}
			if(helm != null) {
				if(!Equipment.isFullMask(helm.getDefinition())) {
					bldr.writeShort(0x100 + l.getLooks()[1]);
				} else {
					bldr.writeByte(0);
				}
			} else {
				bldr.writeShort(0x100 + l.getLooks()[1]);
			}
		}
		for(int c : l.getColours()) {
			bldr.writeByte(c);
		}
		bldr.writeShort(0x328); //stand
		bldr.writeShort(0x337);
		bldr.writeShort(0x333); //walk
		bldr.writeShort(0x334); // turn180AnimIndex
		bldr.writeShort(0x335); // turn90CWAnimIndex
		bldr.writeShort(0x336); // turn90CCWAnimIndex
		bldr.writeShort(809); // runAnimIndex
		bldr.writeLong(Misc.stringToLong(p.getUnformattedName()));
		bldr.writeByte((byte) 3); //combat level
		bldr.writeShort(0);
		block.writeByte((byte) (bldr.position() & 0xFF));
		block.writeBytes(bldr.getBuffer());
	}

}
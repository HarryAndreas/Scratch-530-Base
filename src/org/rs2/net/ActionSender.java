package org.rs2.net;

import org.jboss.netty.channel.ChannelFutureListener;
import org.rs2.model.MapDataManager;
import org.rs2.model.RSTile;
import org.rs2.model.item.Container;
import org.rs2.model.item.Item;
import org.rs2.model.item.ground.GroundItem;
import org.rs2.model.player.Player;
import org.rs2.net.msg.Message.PacketType;
import org.rs2.net.msg.MessageBuilder;
import org.rs2.util.Configuration;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class ActionSender {
	
	/**
	 * Sends the login configurations
	 * @param p
	 */
	public static void sendLogin(Player p) {
		sendMapRegion(p);
		sendPane(p, 548);
		for(int[] i : Configuration.TAB_DATA) {
			sendTab(p, i[0], i[1]);
		}
		p.getEquipment().refresh();
		p.getInventory().refresh();
		p.getLevels().refresh();
		sendPlayerOption(p, "Trade With", false, 2);
		sendPlayerOption(p, "Follow", false, 3);
		sendMessage(p, "Welcome to RuneCore.");
	}
	
	/**
	 * Sends a configuration
	 * @param config The configuration to set
	 * @param value The value of the configuration
	 */
	public static void sendConfig(Player p, int config, int value) {
		if(value < 128) {
			MessageBuilder bldr = new MessageBuilder(100);
			bldr.writeShortA(config);
			bldr.writeByteA(value); 
			p.getSession().write(bldr.toMessage());
		} else {
			MessageBuilder bldr = new MessageBuilder(161);
			bldr.writeShort(config);
			bldr.writeInt1(value);
			p.getSession().write(bldr.toMessage());
		}
	}
	
	/**
	 * Sends a player option
	 * @param p The player
	 * @param display The option to display
	 * @param atTop Is the option at the top
	 * @param optionSlot The slot in which to display the option
	 */
	public static void sendPlayerOption(Player p, String display, boolean atTop, int optionSlot) {
		MessageBuilder bldr = new MessageBuilder(252, PacketType.VAR_BYTE);
		bldr.writeByteC(atTop ? 1 : 0);
		bldr.writeRS2String(display);
		bldr.writeByteC(optionSlot);
		p.getSession().getChannel().write(bldr.toMessage());
	}
	
	/**
	 * Sends a ground item
	 * @param p The player
	 * @param i The ground item
	 */
	public static void sendGroundItem(Player p, GroundItem i) {
		sendLocation(p, i.getLocation());
		MessageBuilder bldr = new MessageBuilder(25);
		bldr.writeLEShortA(i.getItemAmount());
		bldr.writeByte(0);
		bldr.writeLEShortA(i.getItemId());
		p.getSession().getChannel().write(bldr.toMessage());
	}
	
	/**
	 * Removes a ground item
	 * @param p The player
	 * @param i The ground item
	 */
	public static void removeGroundItem(Player p, GroundItem i) {
		sendLocation(p, i.getLocation());
		MessageBuilder bldr = new MessageBuilder(201);
		bldr.writeByte(0);
		bldr.writeShort(i.getItemId());
		p.getSession().getChannel().write(bldr.toMessage());
	}
	
	/**
	 * Sends the location
	 */
	public static void sendLocation(Player p, RSTile location) {
		MessageBuilder bldr = new MessageBuilder(177);
		int regionX = p.getUpdateFlags().getLastKnownLocation().getRegionX();
		int regionY = p.getUpdateFlags().getLastKnownLocation().getRegionY();
		bldr.writeByte((location.getY()-((regionY-6)*8)));
		bldr.writeByteS((location.getX()-((regionX-6)*8)));
		p.getSession().getChannel().write(bldr.toMessage());
	}
	
	/**
	 * Sends a message
	 * @param p The player 
	 * @param msg The msg to send
	 */
	public static void sendMessage(Player p, String msg) {
		MessageBuilder bldr = new MessageBuilder(218, PacketType.VAR_BYTE);
		bldr.writeRS2String(msg);
		p.getSession().getChannel().write(bldr.toMessage());
	}
	
	/**
	 * Sends a skill level
	 * @param p The player 
	 * @param level The level to send
	 */
	public static void sendSkillLevel(Player p, int level) {
		MessageBuilder bldr = new MessageBuilder(217);
		bldr.writeByteC(p.getLevels().getLevel()[level]);
		bldr.writeInt2((int)p.getLevels().getXp()[level]);
		bldr.writeByteC(level);
		p.getSession().getChannel().write(bldr.toMessage());
	}
	
	/**
	 * Sends an item container
	 * @param p The player
	 * @param interfaceID The interface ID
	 * @param items The items to send
	 */
	public static void sendItemContainer(Player p, int interfaceID, int child, int type, Container<Item> items) {
		MessageBuilder bldr = new MessageBuilder(255, PacketType.VAR_SHORT);
		bldr.writeShort(interfaceID);
		bldr.writeShort(child);
		bldr.writeShort(type);
		bldr.writeShort(items.getSize());
		for(Item i : items.getItems()) {
			int id = -1;
			int am = 0;
			if(i != null) {
				id = i.getItemId();
				am = i.getItemAmount();
			}
			if(am > 254) {
				bldr.writeByteS(255);
				bldr.writeInt2(am);
			} else {
				bldr.writeByteS(am);
			}
			bldr.writeLEShort(id + 1);
		}
		p.getSession().getChannel().write(bldr.toMessage());
	}
	
	/**
	 * Logs out the player
	 * @param p
	 */
	public static void sendLogoutPacket(Player p) {
		p.getSession().getChannel().write(new MessageBuilder(104).toMessage()).addListener(ChannelFutureListener.CLOSE);
	}
	
	/**
	 * Sends a tab
	 * @param p The player
	 * @param tabID The tab id
	 * @param child The child id
	 */
	public static void sendTab(Player p, int tabID, int child) {
		sendInterface(p, 1, child == 137 ? 752 : 548, tabID, child);
	}
	
	/**
	 * Sends an interface
	 * @param p
	 * @param showId
	 * @param windowId
	 * @param interfaceId
	 * @param childId
	 */
	public static void sendInterface(Player p, int showId, int windowId, int interfaceId, int childId) {
		MessageBuilder bldr = new MessageBuilder(93);
		bldr.writeShort(childId);
		bldr.writeByteA(showId);
		bldr.writeShort(windowId);
		bldr.writeShort(interfaceId);
		p.getSession().getChannel().write(bldr.toMessage());
	}
	
	public static void sendString(Player p, String string, int interfaceId, int childId) {
		int sSize = string.length() + 5;
		MessageBuilder spb = new MessageBuilder(179)
		.writeByte((byte) (sSize / 256))
		.writeByte((byte) (sSize % 256))
		.writeRS2String(string)
		.writeShort(childId)
		.writeShort(interfaceId);
		p.getSession().getChannel().write(spb.toMessage());
	}
	
	/**
	 * Sends a window pane
	 * @param p The player
	 * @param pane The pane id
	 */
	public static void sendPane(Player p,int pane) {
		MessageBuilder bldr = new MessageBuilder(239);
		bldr.writeShort(pane);
		bldr.writeByteA(0);
		p.getSession().getChannel().write(bldr.toMessage());
	}
	
	/**
	 * Sends the map region
	 * @param p
	 */
	public static void sendMapRegion(Player p) {
		MessageBuilder bldr = new MessageBuilder(142, PacketType.VAR_SHORT);
		bldr.writeShortA(p.getLocation().getRegionX());
		bldr.writeLEShortA(p.getLocation().getLocalY());
		bldr.writeShortA(p.getLocation().getLocalX());
		boolean forceSend = true;
		if((((p.getLocation().getRegionX() / 8) == 48) || ((p.getLocation().getRegionX() / 8) == 49)) && ((p.getLocation().getRegionY() / 8) == 48)) {
			forceSend = false;
		}
		if(((p.getLocation().getRegionX() / 8) == 48) && ((p.getLocation().getRegionY() / 8) == 148)) {
			forceSend = false;
		}
		for(int xCalc = (p.getLocation().getRegionX() - 6) / 8; xCalc <= ((p.getLocation().getRegionX() + 6) / 8); xCalc++) {
			for(int yCalc = (p.getLocation().getRegionY() - 6) / 8; yCalc <= ((p.getLocation().getRegionY() + 6) / 8); yCalc++) {
				int region = yCalc + (xCalc << 8); 
				if(forceSend || ((yCalc != 49) && (yCalc != 149) && (yCalc != 147) && (xCalc != 50) && ((xCalc != 49) || (yCalc != 47)))) {
					int[] mapData = MapDataManager.getSingleton().getMapData(region);
					if(mapData == null) {
						mapData = new int[4];
						for(int i = 0; i < 4; i++) {
							mapData[i] = 0;
						}
					}
					bldr.writeInt(mapData[0]);
					bldr.writeInt(mapData[1]);
					bldr.writeInt(mapData[2]);
					bldr.writeInt(mapData[3]);
				}
			}
		}
		bldr.writeByteC(p.getLocation().getZ());
		bldr.writeShort(p.getLocation().getRegionY());
		p.getUpdateFlags().setLastKnownLocation(p.getLocation());
		p.getSession().getChannel().write(bldr.toMessage());
	}

}
package org.rs2.net.packethandler.impl;

import org.rs2.event.Event;
import org.rs2.event.EventManager;
import org.rs2.model.RSTile;
import org.rs2.model.World;
import org.rs2.model.item.Item;
import org.rs2.model.item.ground.GroundItem;
import org.rs2.model.mask.UpdateFlag;
import org.rs2.model.player.GameSession;
import org.rs2.model.player.Player;
import org.rs2.model.player.containers.Equipment;
import org.rs2.net.ActionSender;
import org.rs2.net.msg.Message;
import org.rs2.net.packethandler.PacketHandler;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class ItemActionPacketHandler implements PacketHandler {

	@Override
	public int[] getBinds() {
		return new int[] {
			167, 179, // Swap Items
			3, 203, 152, 211,// Inventory actions
			201 // take
		};
	}

	/**
	 * Handles the packet
	 */
	@Override
	public void handlePacket(Message m, GameSession gs) {
		switch(m.getOpcode()) {
		case 167:
		case 179:
			handleItemSwitch(m, gs);
			break;
		case 3: //equip
			handleItemEquip(m, gs);
			break;
		case 211:
			handleDropItem(m, gs);
			break;
		case 201:
			handleItemPickup(m, gs);
			break;
		}
	}
	
	/**
	 * Handles picking up an item
	 * @param m The packet
	 * @param s The game session
	 */
	private void handleItemPickup(Message m, GameSession s) {
		final Player p = s.getPlayer();
		int x = m.readShortA() & 0xFFFF;
		int y = m.readShort() & 0xFFFF;
		final RSTile location = RSTile.locate(y, x, s.getPlayer().getLocation().getZ());
		final int id = m.readLEShortA() & 0xFFFF;
		final GroundItem i = World.getSingleton().getGroundItemManager().getItem(id, location, p);
		if(i == null) {
			return;
		}
		if(p.getLocation().withinDistance(location, 0)) {
			int result = canPickup(p, i);
			if(result == 1) {
				World.getSingleton().getGroundItemManager().getGroundItems().remove(i);
				ActionSender.removeGroundItem(p, i);
				p.getInventory().refresh();
			}
		} else {
			Event pickup = new Event() {				
				@Override
				public Event tick() {
					setDelay(0);
					setRepeat(true);
					if(!p.getLocation().withinDistance(location, 0)) {
						return this;
					} else {
						int result = canPickup(p, i);
						if(result == 1) {
							World.getSingleton().getGroundItemManager().getGroundItems().remove(i);
							ActionSender.removeGroundItem(p, i);
							p.getInventory().refresh();
							stop();
						}
					}
					return this;
				}
			};
			EventManager.getSingleton().addEvent(pickup);
			p.setDistancedTask(pickup);
		}
	}
	
	private int canPickup(final Player p, final GroundItem i) {
		if(!World.getSingleton().getGroundItemManager().itemExists(i.getItemId(), i.getLocation(), p)) {
			return -1;
		}
		if(!p.getInventory().add(i)) {
			ActionSender.sendMessage(p, "You don't have enough inventory space.");
			return 0;
		}
		return 1;
	}
	
	/**
	 * Drops an item
	 * @param m The packet
	 * @param s The game session
	 */
	private void handleDropItem(Message m, GameSession s) {
		Player p = s.getPlayer();
		m.readInt();
		int slot = m.readLEShortA() & 0xFFFF;
		m.readLEShort();
		if(slot < 0 || slot >= 28 || p.getInventory().get(slot) == null) {
			return;
		}
		Item item = p.getInventory().get(slot);
		World.getSingleton().getGroundItemManager().dropItem(p, item);
		p.getInventory().set(slot, null);
		p.getInventory().refresh();
	}
	
	/**
	 * Handles equipping an item
	 * @param m The packet
	 * @param s The session
	 */
	private void handleItemEquip(Message m, GameSession s) {
		Player  p = s.getPlayer();
		m.readInt(); 
		m.readLEShort();
		int slot = m.readByte() & 0xFF;
		m.readByte();
		if(slot < 0 || slot >= 28) {
			return;
		}
		Item wieldItem = p.getInventory().get(slot);
		if(wieldItem == null)
			return;
		int wieldSlot = Equipment.getItemSlot(wieldItem.getItemId());
		if(wieldSlot == -1)
			return;
		boolean twoHanded = Equipment.isTwoHanded(wieldItem.getDefinition());
		if(twoHanded && p.getInventory().getFreeSlots() < 1 && p.getEquipment().getContainer().get(5) != null) {
			ActionSender.sendMessage(p, "Not enough free space in your inventory.");
			return;
		}
		if(twoHanded && p.getEquipment().getContainer().get(Equipment.SLOT_SHIELD) != null) {
			p.getInventory().add(p.getEquipment().getContainer().get(Equipment.SLOT_SHIELD));
			p.getEquipment().getContainer().set(Equipment.SLOT_SHIELD, null);
		}
		p.getInventory().set(slot, null);
		if(wieldSlot == 3) {
			if(twoHanded && p.getEquipment().getContainer().get(5) != null) {
				if(!p.getInventory().add(p.getEquipment().getContainer().get(5))) {
					p.getInventory().add(p.getEquipment().getContainer().get(5));
					p.getInventory().refresh();
					return;
				}
				p.getEquipment().getContainer().set(5, null);
			}
		} else if(wieldSlot == 5) {
			if(Equipment.isTwoHanded(p.getEquipment().getContainer().get(3).getDefinition()) && p.getEquipment().getContainer().get(3) != null) {
				if(!p.getInventory().add(p.getEquipment().getContainer().get(3))) {
					p.getInventory().add(p.getEquipment().getContainer().get(3));
					p.getInventory().refresh();
					return;
				}
				p.getEquipment().getContainer().set(3, null);
			}
		}
		if(p.getEquipment().getContainer().get(wieldSlot) != null && (wieldItem.getItemId() != p.getEquipment().getContainer().get(wieldSlot).getDefinition().getId() || !wieldItem.getDefinition().isStackable())) {
			p.getInventory().set(slot, p.getEquipment().getContainer().get(wieldSlot));
			p.getEquipment().getContainer().set(wieldSlot, null);
		}
		int oldAmt = 0;
		if(p.getEquipment().getContainer().get(wieldSlot) != null) {
			oldAmt = p.getEquipment().getContainer().get(wieldSlot).getItemAmount();
		}
		Item item2 = new Item(wieldItem.getItemId(), oldAmt+wieldItem.getItemAmount());
		p.getEquipment().getContainer().set(wieldSlot, item2);
		p.getInventory().refresh();
		p.getEquipment().refresh();
		p.getUpdateFlags().flag(UpdateFlag.APPERANCE);
	}
	
	/**
	 * Handles moving items
	 * @param m 
	 * @param s
	 */
	private void handleItemSwitch(Message m, GameSession s) {
		Player p = s.getPlayer();
		int fromId;
		int toId;
		int id;
		switch(m.getOpcode()) {
			case 167:
				toId = m.readLEShortA();
				m.readByte();
				fromId = m.readLEShortA();
				m.readShort();
				id = m.readByte() & 0xFF;
				m.readByte();
				switch(id) {
					case 149:
						if(fromId < 0 || fromId >= 28 || toId < 0 || toId >= 28) {
							break;
						}
						Item from = p.getInventory().get(fromId);
						Item to = p.getInventory().get(toId);
						p.getInventory().set(fromId, to);
						p.getInventory().set(toId, from);
						p.getInventory().refresh();
				break;
				case 179:
					id = m.readInt() >> 16;
					m.readInt();
					fromId = m.readShort() & 0xFFFF;
					toId = m.readLEShort() & 0xFFFF;
					if(id == 763) {
						if(fromId < 0 || fromId >= 28 || toId < 0 || toId >= 28) {
							break;
						}
						from = p.getInventory().get(fromId);
						to = p.getInventory().get(toId);
						p.getInventory().set(fromId, to);
						p.getInventory().set(toId, from);
						p.getInventory().refresh();
					}
				break;
			}
		}
	}

}
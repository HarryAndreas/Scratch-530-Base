package org.rs2.model.player.containers;

import org.rs2.model.item.Container;
import org.rs2.model.item.Item;
import org.rs2.model.player.Player;
import org.rs2.net.ActionSender;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class Inventory extends Container<Item> {

	/**
	 * Player instance
	 */
	private final Player player;

	/**
	 * Construct the instance
	 * @param size
	 * @param alwaysStackable
	 */
	public Inventory(Player p, int size, boolean alwaysStackable) {
		super(size, alwaysStackable);
		this.player = p;
	}
	
	/**
	 * Refresh's the inventory
	 */
	public void refresh() {
		ActionSender.sendItemContainer(getPlayer(), 149, 0, 93, this);
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
	
}
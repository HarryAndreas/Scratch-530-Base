package org.rs2.model.item.ground;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.rs2.model.RSTile;
import org.rs2.model.World;
import org.rs2.model.item.Item;
import org.rs2.model.player.Player;
import org.rs2.net.ActionSender;
import org.rs2.service.logic.LogicService;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class GroundItemManager {
	
	/**
	 * World instance
	 */
	private World worldInstance;
	
	/**
	 * A list of ground items
	 */
	private List<GroundItem> groundItems;
	
	/**
	 * Construct the instance
	 * @param world
	 */
	public GroundItemManager(World world) {
		setWorldInstance(world);
		setGroundItems(new ArrayList<GroundItem>());
	}
	
	/**
	 * Creates a ground item
	 * @param p
	 * @param drop
	 */
	public void dropItem(Player p, Item drop) {
		GroundItem gi = new GroundItem(drop.getItemId(), drop.getItemAmount(), p.getLocation(), p.getUnformattedName());
		getGroundItems().add(gi);
		ActionSender.sendGroundItem(p, gi);
	}
	
	/**
	 * The process of the manager
	 */
	public void tick() {
		Iterator<GroundItem> it$ = getGroundItems().iterator();
		while(it$.hasNext()) {
			GroundItem item = it$.next();
			if(LogicService.getSingleton().getGameTicks() - item.getCreationTick() >= 100) {
				it$.remove();
				if(item.getOwner() instanceof String) {
					String owner = (String) item.getOwner();
					Player p = World.getSingleton().getByName(owner);
					if(p != null) {
						ActionSender.removeGroundItem(p, item);
					}
				}
				continue;
			}
		}
	}
	
	/**
	 * Loads items for players
	 * @param name
	 */
	public void loadItemsForPlayer(Player p, String name) {
		if(getGroundItems().isEmpty())
			return;
		List<GroundItem> list = new ArrayList<GroundItem>();
		//called on login, 2 different threads = synchronization issues
		//this should fix and prevent them
		synchronized(LogicService.getWorkerThread()) {
			for(GroundItem i : getGroundItems()) {
				if(i.getOwner() instanceof String) {
					String owner = (String) i.getOwner();
					if(owner.contentEquals(name)) {
						list.add(i);
					}
				}
			}
		}
		if(list.isEmpty())
			return;
		for(GroundItem i : list) {
			ActionSender.sendGroundItem(p, i);
		}
	}
	
	/**
	 * Checks if an item exists
	 * @param id
	 * @param location
	 */
	public GroundItem getItem(int id, RSTile location, Player p) {
		final String name = p.getUnformattedName();
		for(GroundItem i : getGroundItems()) {
			if(location.equals(i.getLocation())) {
				if(id == i.getItemId()) {
					if(i.getOwner() instanceof String) {
						String owner = (String) i.getOwner();
						if(owner.contentEquals(name)) {
							return i;
						}
					} 
				}
			}
		}
		return null;
	}
	
	/**
	 * Checks if an item exists
	 * @param id
	 * @param location
	 */
	public boolean itemExists(int id, RSTile location, Player p) {
		final String name = p.getUnformattedName();
		for(GroundItem i : getGroundItems()) {
			if(location.equals(i.getLocation())) {
				if(id == i.getItemId()) {
					if(i.getOwner() instanceof String) {
						String owner = (String) i.getOwner();
						if(owner.contentEquals(name)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * @param worldInstance the worldInstance to set
	 */
	public void setWorldInstance(World worldInstance) {
		this.worldInstance = worldInstance;
	}

	/**
	 * @return the worldInstance
	 */
	public World getWorldInstance() {
		return worldInstance;
	}

	/**
	 * @param groundItems the groundItems to set
	 */
	public void setGroundItems(List<GroundItem> groundItems) {
		this.groundItems = groundItems;
	}

	/**
	 * @return the groundItems
	 */
	public List<GroundItem> getGroundItems() {
		return groundItems;
	}

}

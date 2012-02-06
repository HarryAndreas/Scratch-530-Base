package org.rs2.model.item.ground;

import org.rs2.model.RSTile;
import org.rs2.model.item.Item;
import org.rs2.service.logic.LogicService;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class GroundItem extends Item {
	
	/**
	 * The owner of the ground item
	 */
	private Object owner;
	
	/**
	 * The location of the item
	 */
	private RSTile location;
	
	/**
	 * The tick of which the item was created
	 */
	private long creationTick;

	/**
	 * Construct the 
	 * @param id
	 */
	public GroundItem(int id, int am, RSTile location, Object owner) {
		super(id, am);
		setCreationTick(LogicService.getSingleton().getGameTicks());
		setLocation(location);
		setOwner(owner);
	}

	/**
	 * @param creationTick the creationTick to set
	 */
	private void setCreationTick(long creationTick) {
		this.creationTick = creationTick;
	}

	/**
	 * @return the creationTick
	 */
	public long getCreationTick() {
		return creationTick;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(RSTile location) {
		this.location = location;
	}

	/**
	 * @return the location
	 */
	public RSTile getLocation() {
		return location;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Object owner) {
		this.owner = owner;
	}

	/**
	 * @return the owner
	 */
	public Object getOwner() {
		return owner;
	}

}

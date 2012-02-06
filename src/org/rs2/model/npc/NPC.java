package org.rs2.model.npc;

import org.rs2.model.Entity;
import org.rs2.model.RSTile;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class NPC extends Entity {

	/**
	 * Construct instance 
	 * @param id The id of the NPC
	 * @param location Location of the entity
	 */
	public NPC(int id, RSTile location) {
		super();
		if(location != null) {
			setLocation(location);
		}
		setId(id);
	}
	
	/**
	 * The id of the NPC
	 */
	private int id;
	
	/**
	 * Called roughly every 600ms
	 */
	@Override
	public void tick() {
		//Empty for now
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

}

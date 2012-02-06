package org.rs2.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.rs2.model.mask.Damage;
import org.rs2.model.mask.Movement;
import org.rs2.model.mask.UpdateFlag;
import org.rs2.model.mask.UpdateFlags;
import org.rs2.util.Configuration;

/**
 * 508 Base
 * @author Harry Andreas
 */
public abstract class Entity {
	
	/**
	 * Location of the entity
	 */
	private RSTile location;
	
	/**
	 * Location where to teleport to
	 */
	private RSTile teleportToLocation;

	/**
	 * The update flags for the entity
	 */
	private UpdateFlags updateFlags;
	
	/**
	 * The movement instace for the entity
	 */
	private Movement movement;

	/**
	 * A map containing attributes
	 */
	protected transient Map<String, Object> attributes = new HashMap<String, Object>();
	
	/**
	 * The damage queue for the entity
	 */
	private Queue<Damage> damageQueue;
	
	/**
	 * The index of the entity
	 */
	private int index;
	
	/**
	 * Construct the entity
	 * @param index The index to set
	 */
	public Entity() {
		setLocation(new RSTile(Configuration.DEFAULT_X, Configuration.DEFAULT_Y, 0));
		setDamageQueue(new LinkedList<Damage>());
		setUpdateFlags(new UpdateFlags(this));
		setMovement(new Movement(this));
		getUpdateFlags().setLastKnownLocation(getLocation());
	}
	
	/**
	 * Processes the damage queue
	 */
	public void processDamageQueue() {
		if(getDamageQueue().isEmpty())
			return;
		if(getUpdateFlags().getFirstDamage() != null ||
				getUpdateFlags().getSecondDamage() != null) {
			return;
		}
		if(getUpdateFlags().getFirstDamage() == null) {
			damage(getDamageQueue().poll());
			return;
		}
		if(getUpdateFlags().getSecondDamage() == null) {
			damage(getDamageQueue().poll());
			return;
		}
	}
	
	/**
	 * Damages an entity 
	 * @param damage The damage instance
	 */
	public boolean damage(Damage damage) {
		if(getUpdateFlags().getFirstDamage() == null) {
			getUpdateFlags().setFirstDamage(damage);
			getUpdateFlags().flag(UpdateFlag.DAMAGE);
			return true;
		}
		if(getUpdateFlags().getSecondDamage() == null) {
			getUpdateFlags().setSecondDamage(damage);
			getUpdateFlags().flag(UpdateFlag.DAMAGE_TWO);
			return true;
		}
		getDamageQueue().add(damage);
		return false;
	}
	
	/**
	 * Called roughly every 600ms
	 */
	public abstract void tick();
	
	/**
	 * Adds an attribute
	 * @param var
	 * @param value
	 */
	public void addAttribute(String var, Object value) {
		attributes.put(var, value);
	}
	
	/**
	 * Gets an attribute 
	 * @param <T> 
	 * @param string
	 * @param fail
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String string, T fail) {
		T object = (T) attributes.get(string);
		if (object != null) {
			return object;
		}
		return fail;
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
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param updateFlags the updateFlags to set
	 */
	public void setUpdateFlags(UpdateFlags updateFlags) {
		this.updateFlags = updateFlags;
	}

	/**
	 * @return the updateFlags
	 */
	public UpdateFlags getUpdateFlags() {
		return updateFlags;
	}

	/**
	 * @param movement the movement to set
	 */
	public void setMovement(Movement movement) {
		this.movement = movement;
	}

	/**
	 * @return the movement
	 */
	public Movement getMovement() {
		return movement;
	}

	/**
	 * @param teleportToLocation the teleportToLocation to set
	 */
	public void setTeleportToLocation(RSTile teleportToLocation) {
		this.teleportToLocation = teleportToLocation;
	}

	/**
	 * @return the teleportToLocation
	 */
	public RSTile getTeleportToLocation() {
		return teleportToLocation;
	}

	/**
	 * @param damageQueue the damageQueue to set
	 */
	private void setDamageQueue(Queue<Damage> damageQueue) {
		this.damageQueue = damageQueue;
	}

	/**
	 * @return the damageQueue
	 */
	public Queue<Damage> getDamageQueue() {
		return damageQueue;
	}

}
package org.rs2.model.mask;

import org.rs2.model.Entity;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class Damage {
	
	/**
	 * Constuct the damage instance
	 * @param hit The hit amount 
	 * @param type The type of damage
	 */
	public Damage(int hit, DamageType type, Entity dealer) {
		setHit(hit);
		setType(type);
	}
	
	/**
	 * Damage Types
	 * 508 Base
	 * @author Harry Andreas
	 */
	public enum DamageType {
		NO_DAMAGE,
		REGULAR_DAMAGE,
		POISON,
		DISEASE;
	}
	
	/**
	 * The amount of the hit
	 */
	private int hit;
	
	/**
	 * The type of damage
	 */
	private DamageType type;
	
	/**
	 * The dealer of the damage
	 */
	private Entity dealer;

	/**
	 * @param hit the hit to set
	 */
	public void setHit(int hit) {
		this.hit = hit;
	}

	/**
	 * @return the hit
	 */
	public int getHit() {
		return hit;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(DamageType type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public DamageType getType() {
		return type;
	}

	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(Entity dealer) {
		this.dealer = dealer;
	}

	/**
	 * @return the dealer
	 */
	public Entity getDealer() {
		return dealer;
	}

}

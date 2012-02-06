package org.rs2.model.mask;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class Animation {
	
	/**
	 * 
	 */
	private int id, delay;
	
	/**
	 * Constructor
	 * @param id
	 * @param delay
	 */
	private Animation(int id, int delay) {
		setId(id);
		setDelay(delay);
	}
	
	public static Animation createAnimation(int... args) {
		switch(args.length) {
		case 1:
			return new Animation(args[0], 0);
		case 2:
			return new Animation(args[0], args[1]);
		default:
			return null;
		}
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

	/**
	 * @param delay the delay to set
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}

	/**
	 * @return the delay
	 */
	public int getDelay() {
		return delay;
	}
 
}
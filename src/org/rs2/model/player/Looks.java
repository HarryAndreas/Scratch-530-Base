package org.rs2.model.player;

import org.rs2.net.msg.Message;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class Looks {
	
	/**
	 * 
	 */
	private Message cachedAppearance;

	/**
	 * Variables for the 'looks'
	 */
	private boolean asNpc = false;
	private int npcId = -1;
	private int gender = 0;
	private int[] look = new int[7];
	private int[] colour = new int[5];
	
	/**
	 * Construct the default looks of the 
	 * player
	 */
	public Looks() {
		look[1] = 10;
		look[2] = 18;
		look[3] = 26;
		look[4] = 33;
		look[5] = 36;
		look[6] = 42;
		for(int i = 0; i < 5; i++) {
			colour[i] = i*3+2;
		}
	}
	
	/**
	 * Sets the player to an npc
	 * @param id
	 */
	public void setNPC(int id) {
		if(id == -1) {
			setAsNpc(false);
		} else {
			setNpcId(id);
			setAsNpc(true);
		}
	}
	
	public int[] getColours() {
		return colour;
	}
	
	public int[] getLooks() {
		return look;
	}

	/**
	 * @param asNpc the asNpc to set
	 */
	public void setAsNpc(boolean asNpc) {
		this.asNpc = asNpc;
	}

	/**
	 * @return the asNpc
	 */
	public boolean isAsNpc() {
		return asNpc;
	}

	/**
	 * @param npcId the npcId to set
	 */
	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	/**
	 * @return the npcId
	 */
	public int getNpcId() {
		return npcId;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(int gender) {
		this.gender = gender;
	}

	/**
	 * @return the gender
	 */
	public int getGender() {
		return gender;
	}

	public Message getCachedAppearance() {
		return cachedAppearance;
	}

	public void setCachedAppearance(Message cachedAppearance) {
		this.cachedAppearance = cachedAppearance;
	}

}

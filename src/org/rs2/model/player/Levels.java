package org.rs2.model.player;

import org.rs2.net.ActionSender;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class Levels {
	
	/**
	 * Button child and id's for skill guides
	 */
	public static final int[][] SKILL_TAB_BUTTONS =  {
		{ 123, 0 }, { 126, 1 }, { 129, 2 }, { 132, 3 },
		{ 135, 4 }, { 142, 5 }, { 145, 18 }, { 148, 21 },
		{ 124, 6 }, { 127, 7 }, { 130, 8 }, { 133, 9 },
		{ 136, 10 }, { 143, 11 }, { 146, 19 }, { 149, 22 },
		{ 125, 12 }, { 128, 13 }, { 131, 14 }, { 134, 15 },
		{ 137, 16 }, { 144, 17 }, { 147, 20 }, { 148, 24 }
	};
	
	/**
	 * The player
	 */
	private Player player;

	/**
	 * The max level of skills
	 */
	public static final int MAX_SKILLS = 24;
	
	/**
	 * An array of level skills
	 */
	private final int[] level = new int[Levels.MAX_SKILLS];
	
	/**
	 * An array of XP for each level
	 */
	private final double[] xp = new double[Levels.MAX_SKILLS];
	
	/**
	 * Construct the instance
	 * @param p The player of which these levels belong to
	 */
	public Levels(Player p) {
		setPlayer(p);
		for(int i = 0; i < Levels.MAX_SKILLS; i++) {
			if(i == 3) {
				level[3] = 10;
				xp[3] = 1154;
			} else {
				level[i] = 1;
				xp[i] = 0;
			}
		}
	}
	
	/**
	 * Refresh's the skill levels
	 */
	public void refresh() {
		for(int i = 0; i < Levels.MAX_SKILLS; i++) {
			refresh(i);
		}
	}
	
	/**
	 * Refreshes a specified skill
	 * @param i
	 */
	public void refresh(int i) {
		ActionSender.sendSkillLevel(getPlayer(), i);
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the level
	 */
	public int[] getLevel() {
		return level;
	}

	/**
	 * @return the xp
	 */
	public double[] getXp() {
		return xp;
	}

}
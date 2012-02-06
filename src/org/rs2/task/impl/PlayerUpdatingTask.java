package org.rs2.task.impl;

import org.rs2.model.player.NPCUpdating;
import org.rs2.model.player.Player;
import org.rs2.model.player.PlayerUpdating;
import org.rs2.task.Task;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class PlayerUpdatingTask extends Task {
	
	/**
	 * Constructor
	 * @param args
	 */
	public PlayerUpdatingTask(Object... args) {
		super(args);
	}

	/**
	 * Executes the task
	 */
	@Override
	public void execute() {
		Player p = (Player)getArgs()[0];
		PlayerUpdating.updatePlayer(p);
		p.getSession().write(NPCUpdating.update(p));
	}
	
}
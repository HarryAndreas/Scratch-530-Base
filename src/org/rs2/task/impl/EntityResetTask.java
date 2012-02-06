package org.rs2.task.impl;

import org.rs2.model.Entity;
import org.rs2.task.Task;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class EntityResetTask extends Task {
	
	/**
	 * Constructor
	 * @param args
	 */
	public EntityResetTask(Object... args) {
		super(args);
	}

	@Override
	public void execute() {
		Entity e = (Entity)getArgs()[0];
		e.getUpdateFlags().reset();
		e.getMovement().setMapRegionDidChange(false);
		e.getUpdateFlags().setDidTeleport(false);
	}

}
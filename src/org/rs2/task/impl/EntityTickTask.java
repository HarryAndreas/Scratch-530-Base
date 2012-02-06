package org.rs2.task.impl;

import org.rs2.model.Entity;
import org.rs2.task.Task;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class EntityTickTask extends Task {
	
	/**
	 * Constructor
	 * @param args
	 */
	public EntityTickTask(Object... args) {
		super(args);
	}

	@Override
	public void execute() {
		Entity e = (Entity)getArgs()[0];
		e.getMovement().getNextPlayerMovement();
		e.tick();
		 
	}

}
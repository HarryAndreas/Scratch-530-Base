package org.rs2.task;

/**
 * 508 Base
 * @author Harry Andreas
 */
public abstract class Task {
	
	/**
	 * Arguements
	 */
	private Object[] args;
	
	/**
	 * Construct the task
	 */
	public Task(Object... args) {
		setArgs(args);
	}
	
	/**
	 * Executes the task
	 */
	public abstract void execute();

	/**
	 * @param arguements the arguements to set
	 */
	public void setArgs(Object[] arguements) {
		this.args = arguements;
	}

	/**
	 * @return the arguements
	 */
	public Object[] getArgs() {
		return args;
	}

}

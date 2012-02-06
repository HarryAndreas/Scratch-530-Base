package org.rs2.gui.plugin;

/**
 * 508 Base
 * @author Harry Andreas
 */
public interface Plugin {
	
	/**
	 * Executes a plugin
	 * @param args The arguements for the plugin execution
	 * @return Any possible outcome
	 */
	public abstract Object execute(Object... args);

}

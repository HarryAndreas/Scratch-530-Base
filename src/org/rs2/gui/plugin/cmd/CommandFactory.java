package org.rs2.gui.plugin.cmd;

import java.util.HashMap;
import java.util.Map;

import org.rs2.gui.ServerWindow;
import org.rs2.gui.plugin.type.CommandPlugin;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class CommandFactory {
	
	/**
	 * Singleton instance
	 */
	private static CommandFactory instance;
	
	/**
	 * A map of commands
	 */
	private final Map<String, CommandPlugin> commands;
	
	/**
	 * Gets the singleton instance
	 * @return
	 */
	public static CommandFactory getSingleton() {
		if(instance == null) {
			instance = new CommandFactory();
		}
		return instance;
	}
	
	/**
	 * Construct the object
	 */
	private CommandFactory() {
		commands = new HashMap<String, CommandPlugin>();
	}
	
	/**
	 * Registers a command
	 * @param cp The command plugin
	 */
	public void register(CommandPlugin cp) {
		for(String s : cp.bindCommands()) {
			commands.put(s.toLowerCase(), cp);
		}
	}
	
	/**
	 * Invokes a command
	 * @param cmd
	 * @param args
	 * @throws Exception 
	 */
	public void invoke(String cmd, Object... args) throws Exception {
		CommandPlugin plugin = commands.get(cmd.toLowerCase());
		if(plugin == null) {
			String text = "Command "+cmd+" doesn't exist!";
			ServerWindow.getSingleton().printCommandText(text);
			throw new Exception(text);
		}
		plugin.execute(args);
	}

}
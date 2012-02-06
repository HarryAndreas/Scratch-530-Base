package org.rs2;

import java.util.logging.Logger;

import org.rs2.cache.FileStore;
import org.rs2.gui.ServerWindow;
import org.rs2.gui.plugin.PluginFactory;
import org.rs2.model.MapDataManager;
import org.rs2.model.World;
import org.rs2.model.item.ItemDefinitionsManager;
import org.rs2.model.player.containers.Equipment;
import org.rs2.mysql.DatabaseConnection;
import org.rs2.mysql.DatabaseManager;
import org.rs2.net.packethandler.PacketManager;
import org.rs2.script.JavaScriptEngine;
import org.rs2.service.logic.LogicService;
import org.rs2.service.login.LoginService;
import org.rs2.service.ondemand.OnDemandService;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class Launch {
	
	/**
	 * Logger instance
	 */
	private static final Logger logger = Logger.getAnonymousLogger();
	
	/**
	 * Is the application set to use GUI?
	 */
	public static boolean guiMode = Boolean.TRUE;
	
	/**
	 * Invoked on launch of the application
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if(args.length < 1) {
			System.err.println("ERROR: USAGE SHOULD BE; <port> <max-players>");
			System.err.println("Starting with default settings <5555> <2000> where 5555 is port & 2000 is max-players");
			args = new String[] { "5555", "2000" };
		}
		for(String arg : args) {
			if(arg.contains("-nogui")) {
				guiMode = Boolean.FALSE;
				break;
			}
		}
		if(guiMode) {
			logger.info("Building GUI...");
			ServerWindow.getSingleton().init();
			PluginFactory.getSingleton().init();
		}
		logger.info("Starting server...");
		World.getSingleton();
		FileStore.getSingleton();
		LoginService.getSingleton().init();
		LogicService.getSingleton().init();
		PacketManager.getSingleton().init();
		OnDemandService.getSingleton().init();
		JavaScriptEngine.getSingleton().init();
		DatabaseManager.getSingleton().addDatabase(DatabaseConnection.create("127.0.0.1", "lolserver", "lollol", "server"), "info");
		DatabaseManager.getSingleton().addDatabase(DatabaseConnection.create("127.0.0.1", "lolserver", "lollol", "server"), "player-loader");
		ItemDefinitionsManager.getSingleton().init();
		Equipment.init();
		MapDataManager.getSingleton().init();
		World.getSingleton().loadNPCs();
		DatabaseManager.getSingleton().kill("info");
		Server.createServer(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		Server.getSingleton().init();
		logger.info("Server online on port "+Server.getSingleton().getPort()+"!");
	}

}
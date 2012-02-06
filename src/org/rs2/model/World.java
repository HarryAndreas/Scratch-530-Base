package org.rs2.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.rs2.Launch;
import org.rs2.gui.ServerWindow;
import org.rs2.model.item.ground.GroundItemManager;
import org.rs2.model.list.EntityList;
import org.rs2.model.npc.NPC;
import org.rs2.model.player.Player;
import org.rs2.mysql.DatabaseConnection;
import org.rs2.mysql.DatabaseManager;
import org.rs2.service.logic.LogicService;
import org.rs2.service.saving.SavingService;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class World {
	
	/**
	 * World instance
	 */
	private static World instance;
	
	/**
	 * The ground item manager instance
	 */
	private GroundItemManager groundItemManager;
	
	/**
	 * A list of registered players
	 */
	private EntityList<Player> registeredPlayers;
	
	/**
	 * A list of registered NPC's
	 */
	private EntityList<NPC> registeredNPCs;
	
	/**
	 * The world logger
	 */
	private final static Logger logger = Logger.getLogger(World.class.getName());
	
	/**
	 * Gets the World singleton
	 * @return
	 */
	public static World getSingleton() {
		if(instance == null) {
			instance = new World();
		}
		return instance;
	}
	
	/**
	 * World constructor
	 */
	private World() {
		setRegisteredPlayers(new EntityList<Player>(2000));
		setRegisteredNPCs(new EntityList<NPC>(10000));
		groundItemManager = new GroundItemManager(this);
	}
	
	/**
	 * Loads npcs from 
	 * @throws SQLException 
	 */
	public void loadNPCs() throws SQLException {
		DatabaseConnection c = DatabaseManager.getSingleton().getConnection("info");
		ResultSet rs = c.getStatement("SELECT * FROM npcspawn").executeQuery();
		while(rs.next()) {
			int id = rs.getInt("id");
			int x = rs.getInt("x");
			int y = rs.getInt("y");
			int z = rs.getInt("z");
			NPC n = new NPC(id, RSTile.locate(x, y, z));
			register(n);
		}
	}
	
	/**
	 * Unregisters a player
	 * @param p
	 */
	public void unregisterPlayer(Player p) {
		SavingService.getSingleton().savePlayer(p);
		p.getSession().getChannel().close();
		p.getSession().getQueuedPackets().clear();
		synchronized(LogicService.getWorkerThread()) {
			getRegisteredPlayers().remove(p);
		}
		if(Launch.guiMode) {
			ServerWindow.getSingleton().updatePlayersOnline(getRegisteredPlayers().size());
		}
		getLogger().info("Unregistered player ["+p.getUnformattedName()+" @ index "+p.getIndex()+"] successfully!");
	}
	
	/**
	 * Checks if the player exists logged in
	 * @param name The name of the player
	 * @return If its logged in
	 */
	public boolean checkExistance(String name) {
		for(Player p : getRegisteredPlayers()) {
			if(p.getUnformattedName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets a player by name
	 * @param name The name of the player
	 * @return The player instance if any
	 */
	public Player getByName(String name) {
		for(Player p : getRegisteredPlayers()) {
			if(p.getUnformattedName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}
 	
	/**
	 * Register entity
	 * @param e Entity instance
	 * @return Could it be registered
	 */
	public boolean register(Entity e) {
		if(e instanceof Player) {
			synchronized(LogicService.getWorkerThread()) {
				boolean returnThis = getRegisteredPlayers().add((Player)e);
				if(Launch.guiMode) {
					ServerWindow.getSingleton().updatePlayersOnline(getRegisteredPlayers().size());
				}
				return returnThis;
			}
		}
		if(e instanceof NPC) {
			return getRegisteredNPCs().add((NPC) e);
		}
		return false;
	}

	/**
	 * @param registeredPlayers the registeredPlayers to set
	 */
	private void setRegisteredPlayers(EntityList<Player> registeredPlayers) {
		this.registeredPlayers = registeredPlayers;
	}

	/**
	 * @return the registeredPlayers
	 */
	public EntityList<Player> getRegisteredPlayers() {
		return registeredPlayers;
	}

	/**
	 * @return the logger
	 */
	private static Logger getLogger() {
		return logger;
	}
	
	/**
	 * @return the groundItemManager
	 */
	public GroundItemManager getGroundItemManager() {
		return groundItemManager;
	}

	/**
	 * @param registeredNPCs the registeredNPCs to set
	 */
	public void setRegisteredNPCs(EntityList<NPC> registeredNPCs) {
		this.registeredNPCs = registeredNPCs;
	}

	/**
	 * @return the registeredNPCs
	 */
	public EntityList<NPC> getRegisteredNPCs() {
		return registeredNPCs;
	}

}
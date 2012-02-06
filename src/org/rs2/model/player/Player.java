package org.rs2.model.player;

import java.util.LinkedList;
import java.util.List;

import org.rs2.event.Event;
import org.rs2.event.EventManager;
import org.rs2.model.Entity;
import org.rs2.model.npc.NPC;
import org.rs2.model.player.containers.Equipment;
import org.rs2.model.player.containers.Inventory;
import org.rs2.task.impl.*;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class Player extends Entity {
	
	/**
	 * Name variables 
	 */
	private String formattedName;
	private String unformattedName;
	private String hashedPassword;
	
	/**
	 * Rights for the player
	 */
	private int rights = 0;
	
	/**
	 * Game session
	 */
	private GameSession session;
	
	/**
	 * The 'looks' of the player
	 */
	private Looks looks;
	
	/**
	 * Containers
	 */
	private Equipment equipment;
	private Inventory inventory;
	
	/**
	 * The levels of the player
	 */
	private Levels levels;
	
	/**
	 * Tasks for the world process
	 */
	private final PlayerPacketProcessTask packetTask;
	private final EntityTickTask tickTask;
	private final PlayerUpdatingTask updateTask;
	private final EntityResetTask resetTask;
	
	/**
	 * The instance of the event that is set
	 * for a distanced task, i.e walking to an object
	 */
	private Event distancedTask;
	
	/**
	 * A list of regional players
	 */
	private final List<Player> regionalPlayers = new LinkedList<Player>();

	/**
	 * A list of regional NPCs
	 */
	private final List<NPC> regionalNPCs = new LinkedList<NPC>();
	
	/**
	 * Constructor
	 */
	public Player(GameSession session, int rights) {
		super();
		setRights(rights);
		setSession(session);
		getSession().setPlayer(this);
		setLooks(new Looks());
		setEquipment(new Equipment(this));
		setInventory(new Inventory(this, 28, false));
		setLevels(new Levels(this));
		packetTask = new PlayerPacketProcessTask(this);
		tickTask = new EntityTickTask(this);
		updateTask = new PlayerUpdatingTask(this);
		resetTask = new EntityResetTask(this);
	}

	/**
	 * Called roughtly every 600ms
	 */
	@Override
	public void tick() {
		//Empty for now
	}

	/**
	 * @param formattedName the formattedName to set
	 */
	public void setFormattedName(String formattedName) {
		this.formattedName = formattedName;
	}

	/**
	 * @return the formattedName
	 */
	public String getFormattedName() {
		return formattedName;
	}

	/**
	 * @param unformattedName the unformattedName to set
	 */
	public void setUnformattedName(String unformattedName) {
		this.unformattedName = unformattedName;
	}

	/**
	 * @return the unformattedName
	 */
	public String getUnformattedName() {
		return unformattedName;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(GameSession session) {
		this.session = session;
	}

	/**
	 * @return the session
	 */
	public GameSession getSession() {
		return session;
	}

	/**
	 * @return the tickTask
	 */
	public EntityTickTask getTickTask() {
		return tickTask;
	}

	/**
	 * @return the updateTask
	 */
	public PlayerUpdatingTask getUpdateTask() {
		return updateTask;
	}

	/**
	 * @return the resetTask
	 */
	public EntityResetTask getResetTask() {
		return resetTask;
	}

	/**
	 * @return the packetTask
	 */
	public PlayerPacketProcessTask getPacketTask() {
		return packetTask;
	}

	/**
	 * @param looks the looks to set
	 */
	public void setLooks(Looks looks) {
		this.looks = looks;
	}

	/**
	 * @return the looks
	 */
	public Looks getLooks() {
		return looks;
	}

	/**
	 * @param equipment the equipment to set
	 */
	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	/**
	 * @return the equipment
	 */
	public Equipment getEquipment() {
		return equipment;
	}

	/**
	 * @param rights the rights to set
	 */
	public void setRights(int rights) {
		this.rights = rights;
	}

	/**
	 * @return the rights
	 */
	public int getRights() {
		return rights;
	}

	/**
	 * @return the regionalPlayers
	 */
	public List<Player> getRegionalPlayers() {
		return regionalPlayers;
	}

	/**
	 * @param hashedPassword the hashedPassword to set
	 */
	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	/**
	 * @return the hashedPassword
	 */
	public String getHashedPassword() {
		return hashedPassword;
	}

	/**
	 * @param levels the levels to set
	 */
	public void setLevels(Levels levels) {
		this.levels = levels;
	}

	/**
	 * @return the levels
	 */
	public Levels getLevels() {
		return levels;
	}

	/**
	 * @param inventory the inventory to set
	 */
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	/**
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * @param distancedTask the distancedTask to set
	 */
	public void setDistancedTask(Event distancedTask) {
		if(this.distancedTask != null) {
			EventManager.getSingleton().removeEvent(this.distancedTask);
		}
		this.distancedTask = distancedTask;
	}

	/**
	 * @return the distancedTask
	 */
	public Event getDistancedTask() {
		return distancedTask;
	}

	/**
	 * @return the regionalNPCs
	 */
	public List<NPC> getRegionalNPCs() {
		return regionalNPCs;
	}
	
}
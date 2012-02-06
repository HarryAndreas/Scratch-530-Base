package org.rs2.model.player.containers;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.rs2.model.item.*;
import org.rs2.model.player.Player;
import org.rs2.mysql.*;
import org.rs2.net.ActionSender;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class Equipment {
	
	/**
	 * The player instance
	 */
	private Player player;
	
	/**
	 * Construct the equipment
	 * @param p
	 */
	public Equipment(Player p) {
		setPlayer(p);
	}
	
	/**
	 * Holds values of wield slots
	 */
	private static Map<Integer, Integer> wieldSlots;
	
	/**
	 * Loads the wield slots for item equiping
	 * @throws Exception
	 */
	public static void init() throws Exception {
		if(wieldSlots != null) {
			throw new Exception("Wield slots are already loaded!");
		}
		wieldSlots = new HashMap<Integer, Integer>();
		DatabaseConnection con = DatabaseManager.getSingleton().getConnection("info");
		ResultSet rs = con.getStatement("SELECT * from `equipment-slots`").executeQuery();
		while(rs.next()) {
			wieldSlots.put(rs.getInt("id"), rs.getInt("slot-id"));
		}
	}
	
	/**
	 * Gets the item slot
	 * @param id The id of the item to find the slot for
	 * @return The slot 
	 */
	public static int getItemSlot(int id) {
		return wieldSlots.get(id);
	}
	
	/**
	 * Refreshs the equipment
	 */
	public void refresh() {
		ActionSender.sendItemContainer(getPlayer(), 387, 28, 94, getContainer());
	}
	
	/**
	 * Is the item full body
	 * @param def
	 * @return
	 */
	public static boolean isFullBody(ItemDefinition def) {
		String weapon = def.getName();
		for (int i = 0; i < FULL_BODY.length; i++) {
			if (weapon.contains(FULL_BODY[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if an item is 2 handed
	 * @author Graham Edgecombe
	 * @param def The ItemDefinition
	 * @return If it's two handed
	 */
	public static boolean isTwoHanded(ItemDefinition def) {
		String wepEquiped = def.getName();
		if (wepEquiped.endsWith("2h sword"))
			return true;
		else if (wepEquiped.endsWith("longbow"))
			return true;
		else if (wepEquiped.equals("Seercull"))
			return true;
		else if (wepEquiped.endsWith("shortbow"))
			return true;
		else if (wepEquiped.endsWith("Longbow"))
			return true;
		else if (wepEquiped.endsWith("Shortbow"))
			return true;
		else if (wepEquiped.endsWith("bow full"))
			return true;
		else if (wepEquiped.endsWith("halberd"))
			return true;
		else if (wepEquiped.equals("Granite maul"))
			return true;
		else if (wepEquiped.equals("Karils crossbow"))
			return true;
		else if (wepEquiped.equals("Torags hammers"))
			return true;
		else if (wepEquiped.equals("Veracs flail"))
			return true;
		else if (wepEquiped.equals("Dharoks greataxe"))
			return true;
		else if (wepEquiped.equals("Guthans warspear"))
			return true;
		else if (wepEquiped.equals("Tzhaar-ket-om"))
			return true;
		else if (wepEquiped.endsWith("godsword"))
			return true;
		else if (wepEquiped.equals("Saradomin sword"))
			return true;
		else
			return false;
	}

	/**
	 * Is the item a full hat
	 * @param def
	 * @return
	 */
	public static boolean isFullHat(ItemDefinition def) {
		String weapon = def.getName();
		for (int i = 0; i < FULL_HAT.length; i++) {
			if (weapon.endsWith(FULL_HAT[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Is the full mask
	 * @param def
	 * @return
	 */
	public static boolean isFullMask(ItemDefinition def) {
		String weapon = def.getName();
		for (int i = 0; i < FULL_MASK.length; i++) {
			if (weapon.endsWith(FULL_MASK[i])) {
				return true;
			}
		}
		return false;
	}
		
	/**
	 * Equipment container
	 */
	private Container<Item> container = new Container<Item>(SIZE, false);

	/**
	 * @param container the container to set
	 */
	public void setContainer(Container<Item> container) {
		this.container = container;
	}

	/**
	 * @return the container
	 */
	public Container<Item> getContainer() {
		return container;
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
	 * Static variables
	 */
	public static final int SLOT_HAT = 0, SLOT_CAPE = 1, SLOT_AMULET = 2, 
		SLOT_WEAPON = 3, SLOT_CHEST = 4, SLOT_SHIELD = 5, SLOT_LEGS = 7, 
		SLOT_HANDS = 9, SLOT_FEET = 10, SLOT_RING = 12, SLOT_ARROWS = 13;
	public static final int SIZE = 14;
	private static final String[] FULL_BODY = {"top","shirt","platebody","Ahrims robetop","Karils leathertop","brassard","Robe top","robetop","platebody (t)","platebody (g)","chestplate","torso"};
	private static final String[] FULL_HAT = {"med helm","coif","Dharoks helm","hood","Initiate helm","Coif","Helm of neitiznot"};
	private static final String[] FULL_MASK = {"full helm","mask","Veracs helm","Guthans helm","Torags helm","Karils coif","full helm (t)","full helm (g)","mask"};


}
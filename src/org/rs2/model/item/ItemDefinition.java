package org.rs2.model.item;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class ItemDefinition {
	
	/**
	 * Variables for item definition
	 */
	private final int id, wieldId;
	private final String name, examine;
	private final boolean noted, stackable;
	private final int[] bonuses;
	
	/**
	 * Construct the definition
	 * @param id The id of the item
	 * @param name The name of the item 
	 * @param stack Does the item stack
	 * @param noted Is the item noted?
	 * @param wield The wield id for updating
	 */
	public ItemDefinition(int id, String name, String examine, boolean noted, boolean stackable, int wield, int[] bonuses) {
		this.id = id;
		this.name = name;
		this.noted = noted;
		this.stackable = stackable;
		this.wieldId = wield;
		this.examine = examine;
		this.bonuses = bonuses;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the wieldId
	 */
	public int getWieldId() {
		return wieldId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the noted
	 */
	public boolean isNoted() {
		return noted;
	}

	/**
	 * @return the stackable
	 */
	public boolean isStackable() {
		return stackable;
	}

	/**
	 * @return the examine
	 */
	public String getExamine() {
		return examine;
	}

	/**
	 * @return the bonuses
	 */
	public int[] getBonuses() {
		return bonuses;
	}

}
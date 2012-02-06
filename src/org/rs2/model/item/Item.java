package org.rs2.model.item;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class Item {
	
	/**
	 * Variables for the server
	 */
	private int itemId, itemAmount;
	
	/**
	 * Definition
	 */
	private ItemDefinition definition;
	
	/**
	 * 
	 * @param id
	 */
	public Item(int id) {
		setItemId(id);
		setItemAmount(1);
		setDefinition(ItemDefinitionsManager.getSingleton().getDefinition(id));
	}
	
	/**
	 * Constructor
	 * @param itemId
	 * @param amount
	 */
	public Item(int itemId, int amount) {
		setItemId(itemId);
		setItemAmount(amount);
		setDefinition(ItemDefinitionsManager.getSingleton().getDefinition(itemId));
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * @param itemAmount the itemAmount to set
	 */
	public void setItemAmount(int itemAmount) {
		this.itemAmount = itemAmount;
	}

	/**
	 * @return the itemAmount
	 */
	public int getItemAmount() {
		return itemAmount;
	}

	/**
	 * @param definition the definition to set
	 */
	public void setDefinition(ItemDefinition definition) {
		this.definition = definition;
	}

	/**
	 * @return the definition
	 */
	public ItemDefinition getDefinition() {
		return definition;
	}

}
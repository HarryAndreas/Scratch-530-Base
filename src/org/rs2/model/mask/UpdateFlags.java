package org.rs2.model.mask;

import java.util.BitSet;

import org.rs2.model.Entity;
import org.rs2.model.RSTile;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class UpdateFlags {
	
	/**
	 * The entity this class is for
	 */
	private Entity entity;
	
	/**
	 * A bitset of flagged update flags
	 */
	private final BitSet updateFlags = new BitSet();
	
	/**
	 * Sets the last known location for the entity
	 */
	private RSTile lastKnownLocation;
	
	/**
	 * Did the player teleport
	 */
	private boolean didTeleport = Boolean.FALSE;
	
	/**
	 * Instances for updating
	 */
	private Animation currentAnimation;
	private ChatMessage currentChat;
	private Damage firstDamage;
	private Damage secondDamage;
	
	/**
	 * Constructor
	 * @param entity The entity these flags are for
	 */
	public UpdateFlags(Entity entity) {
		setEntity(entity);
	}
	
	/**
	 * Flags an update mask
	 * @param flag
	 * @param seg
	 */
	public void flag(UpdateFlag flag) {
		getUpdateFlags().set(flag.ordinal());
	}
	
	/**
	 * Is an update required
	 */
	public boolean updateRequired() {
		return !getUpdateFlags().isEmpty();
	}
	
	/**
	 * Sends a chat message
	 * @param msg
	 */
	public void sendChat(ChatMessage msg) {
		getUpdateFlags().set(UpdateFlag.CHAT.ordinal());
		setCurrentChat(msg);
	}
	
	/**
	 * Is this flag flagged?
	 * @param flag The flag
	 * @return Is it flagged?
	 */
	public boolean isFlagged(UpdateFlag flag) {
		return getUpdateFlags().get(flag.ordinal());
	}
	
	/**
	 * Resets the update flags
	 */
	public void reset() {
		getUpdateFlags().clear();
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * @return the entity
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * @return the updateFlags
	 */
	public BitSet getUpdateFlags() {
		return updateFlags;
	}

	/**
	 * @param lastKnownLocation the lastKnownLocation to set
	 */
	public void setLastKnownLocation(RSTile lastKnownLocation) {
		this.lastKnownLocation = lastKnownLocation;
	}

	/**
	 * @return the lastKnownLocation
	 */
	public RSTile getLastKnownLocation() {
		return lastKnownLocation;
	}

	/**
	 * @param didTeleport the didTeleport to set
	 */
	public void setDidTeleport(boolean didTeleport) {
		this.didTeleport = didTeleport;
	}

	/**
	 * @return the didTeleport
	 */
	public boolean isDidTeleport() {
		return didTeleport;
	}

	/**
	 * @param currentAnimation the currentAnimation to set
	 */
	public void setCurrentAnimation(Animation currentAnimation) {
		this.currentAnimation = currentAnimation;
	}

	/**
	 * @return the currentAnimation
	 */
	public Animation getCurrentAnimation() {
		return currentAnimation;
	}

	/**
	 * @param currentChat the currentChat to set
	 */
	public void setCurrentChat(ChatMessage currentChat) {
		this.currentChat = currentChat;
	}

	/**
	 * @return the currentChat
	 */
	public ChatMessage getCurrentChat() {
		return currentChat;
	}

	/**
	 * @param firstDamage the firstDamage to set
	 */
	public void setFirstDamage(Damage firstDamage) {
		this.firstDamage = firstDamage;
	}

	/**
	 * @return the firstDamage
	 */
	public Damage getFirstDamage() {
		return firstDamage;
	}

	/**
	 * @param secondDamage the secondDamage to set
	 */
	public void setSecondDamage(Damage secondDamage) {
		this.secondDamage = secondDamage;
	}

	/**
	 * @return the secondDamage
	 */
	public Damage getSecondDamage() {
		return secondDamage;
	}

}
package org.rs2.model.mask;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class ChatMessage {
	
	/**
	 * Variables for the mask block
	 */
	private final int effect;
	private final String chatMsg;
	
	/**
	 * Construct the chat msg
	 * @param effect
	 * @param msg
	 */
	public ChatMessage(int effect, String msg) {
		this.effect = effect;
		this.chatMsg = msg;
	}

	/**
	 * @return the effect
	 */
	public int getEffect() {
		return effect;
	}

	/**
	 * @return the chatMsg
	 */
	public String getChatMsg() {
		return chatMsg;
	}

}

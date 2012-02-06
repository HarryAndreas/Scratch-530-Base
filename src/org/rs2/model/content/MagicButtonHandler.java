package org.rs2.model.content;

import org.rs2.model.RSTile;
import org.rs2.model.player.Player;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class MagicButtonHandler {
	
	/**
	 * Handles a magic action button
	 * @param interfaceID
	 */
	public static void handleButton(Player p, int interfaceID, int childID) {
		if(interfaceID == 192) {
			switch(childID) {
			case 0:
				p.setTeleportToLocation(RSTile.locate(3300, 3300, 0));
			}
		}
	}

}

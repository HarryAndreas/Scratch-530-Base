package org.rs2.model.mask;

import org.rs2.model.Entity;
import org.rs2.model.RSTile;
import org.rs2.model.player.Player;
import org.rs2.net.ActionSender;
import org.rs2.util.Misc;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class Movement {
	
	/**
	 * The entity this instance is for
	 */
	private Entity entity;	

	/**
	 * Did the map region change?
	 */
	private boolean mapRegionDidChange = Boolean.FALSE;
	
	/**
	 * Sprites for movement
	 */
	private int walkingSprite = -1;
	private int runningSprite = -1;
	
	/**
	 * A point
	 * 508 Base
	 * @author Harry Andreas
	 */
	private class Point {
		private int x;
		private int y;
		private int dir;
	}
	
	/**
	 * Size of the walking queue
	 */
	private static final int SIZE = 50;
	
	/**
	 * Variables for movement
	 */
	public int wQueueReadPtr = 0;
	public int wQueueWritePtr = 0;
	public Point[] walkingQueue = new Point[SIZE];
	
	/**
	 * Variables for running
	 */
	private boolean isRunning = false, isRunToggled = false;
	
	
	/**
	 * Toggles run
	 */
	public void toggleRun() {
		isRunToggled = !isRunToggled;
		sendRunToggled();
	}
	
	/**
	 * Sends configuration for running buttons
	 */
	private void sendRunToggled() {
		if(getEntity() instanceof Player) {
			ActionSender.sendConfig((Player)getEntity(), 173, isRunToggled() ? 1 : 0);
		}
	}
	
	public boolean isRunToggled() {
		return isRunToggled;
	}
	
	public void setRunToggled(boolean isRunToggled) {
		this.isRunToggled = isRunToggled;
	}
	
	/**
	 * Construct the class
	 * @param e
	 */
	public Movement(Entity e) {
		setEntity(e);
		for(int i = 0; i < SIZE; i++) {
			walkingQueue[i] = new Point();
			walkingQueue[i].x   = 0;
			walkingQueue[i].y   = 0;
			walkingQueue[i].dir = -1;
		}
		reset();
	}
	
	/**
	 * Sets the entity as running
	 * @param isRunning
	 */
	public void setIsRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
	/**
	 * Resets the walking queue
	 */
	public void reset() {
		walkingQueue[0].x   = entity.getLocation().getLocalX();
		walkingQueue[0].y   = entity.getLocation().getLocalY();
		walkingQueue[0].dir = -1;
		wQueueReadPtr = wQueueWritePtr = 1;
	}
	
	/**
	 * Adds a path step to the walking queue
	 * @param x
	 * @param y
	 */
	public void addToWalkingQueue(int x, int y) {
		int diffX = x - walkingQueue[wQueueWritePtr - 1].x, diffY = y - walkingQueue[wQueueWritePtr - 1].y;
		int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (int i = 0; i < max; i++) {
			if (diffX < 0)
				diffX++;
			else if (diffX > 0)
				diffX--;
			if (diffY < 0)
				diffY++;
			else if (diffY > 0)
				diffY--;
			addStepToWalkingQueue(x - diffX, y - diffY);
		}
	}
	
	/**
	 * Adds a step to the walking queue
	 * @param x
	 * @param y
	 */
	public void addStepToWalkingQueue(int x, int y) {
		int diffX = x - walkingQueue[wQueueWritePtr - 1].x, diffY = y - walkingQueue[wQueueWritePtr - 1].y;
		int dir = Misc.direction(diffX, diffY);
		if (wQueueWritePtr >= SIZE) {
			return;
		}
		if (dir != -1) {
			walkingQueue[wQueueWritePtr].x = x;
			walkingQueue[wQueueWritePtr].y = y;
			walkingQueue[wQueueWritePtr++].dir = dir;
		}
	}
	
	public void getNextPlayerMovement() {
		setWalkingSprite(-1);
		setRunningSprite(-1);
		if(entity.getTeleportToLocation() != null) {
			reset();
			RSTile lastRegion = entity.getLocation();
			int rx = lastRegion.getRegionX();
			int ry = lastRegion.getRegionY();
			entity.setLocation(entity.getTeleportToLocation());
			entity.setTeleportToLocation(null);
			entity.getUpdateFlags().setDidTeleport(true);
			if((rx-entity.getLocation().getRegionX()) >= 4 || (rx-entity.getLocation().getRegionX()) <= -4) {
				setMapRegionDidChange(true);
			}
			if((ry-entity.getLocation().getRegionY()) >= 4 || (ry-entity.getLocation().getRegionY()) <= -4) {
				setMapRegionDidChange(true);
			}
		} else {
			RSTile oldLocation = entity.getLocation();
			int walkDir = getNextWalkingDirection();
			int runDir  = -1;
			int energy = 100;
			if(isRunning || isRunToggled) {
				if(energy > 0) {
					runDir = getNextWalkingDirection();
					//if(runDir != -1) {
						//entity.setRunEnergy(entity.getRunEnergy()-1);
					//}
				} else {
					if(isRunToggled) {
						isRunToggled = false;
						//entity.getActionSender().sendConfig(173, 0);
						isRunning = false;
					}
					isRunning = false;
				}
			}
			RSTile lastRegion = entity.getUpdateFlags().getLastKnownLocation();
			if(lastRegion != null) {
				int rx = lastRegion.getRegionX();
				int ry = lastRegion.getRegionY();
				if((rx-entity.getLocation().getRegionX()) >= 4) {
					setMapRegionDidChange(true);
				} else if((rx-entity.getLocation().getRegionX()) <= -4) {
					setMapRegionDidChange(true);
				}
				if((ry-entity.getLocation().getRegionY()) >= 4) {
					setMapRegionDidChange(true);
				} else if((ry-entity.getLocation().getRegionY()) <= -4) {
					setMapRegionDidChange(true);
				}
			} else {
				setMapRegionDidChange(true);
			}
			if(isMapRegionDidChange()) {
				if(walkDir != -1) {
					wQueueReadPtr--;
				}
				if(runDir != -1) {
					wQueueReadPtr--;
				}
				walkDir = -1;
				runDir = -1;
				entity.setLocation(oldLocation);
			}
			setWalkingSprite(walkDir);
			setRunningSprite(runDir);
		}
	}
	
	private int getNextWalkingDirection() {
		if (wQueueReadPtr == wQueueWritePtr) {
			return -1;
		}
		int dir = walkingQueue[wQueueReadPtr++].dir;
		int xdiff = Misc.DIRECTION_DELTA_X[dir];
		int ydiff = Misc.DIRECTION_DELTA_Y[dir];
		RSTile newLocation = RSTile.locate(entity.getLocation().getX()+xdiff, entity.getLocation().getY()+ydiff, entity.getLocation().getZ());
		entity.setLocation(newLocation);
		return dir;
	}

	public boolean isRunning() {
		return isRunning;
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
	 * @param walkingSprite the walkingSprite to set
	 */
	public void setWalkingSprite(int walkingSprite) {
		this.walkingSprite = walkingSprite;
	}

	/**
	 * @return the walkingSprite
	 */
	public int getWalkingSprite() {
		return walkingSprite;
	}

	/**
	 * @param runningSprite the runningSprite to set
	 */
	public void setRunningSprite(int runningSprite) {
		this.runningSprite = runningSprite;
	}

	/**
	 * @return the runningSprite
	 */
	public int getRunningSprite() {
		return runningSprite;
	}

	/**
	 * @param mapRegionDidChange the mapRegionDidChange to set
	 */
	public void setMapRegionDidChange(boolean mapRegionDidChange) {
		this.mapRegionDidChange = mapRegionDidChange;
	}

	/**
	 * @return the mapRegionDidChange
	 */
	public boolean isMapRegionDidChange() {
		return mapRegionDidChange;
	}

}

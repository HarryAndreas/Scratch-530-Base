package org.rs2.event;

import java.util.ArrayList;
import java.util.List;

/**
 * 508 Base
 * @author Harry Andreas
 */
public abstract class Event {
	
	/**
	 * A list of tasks to run when the event has finished
	 */
	public List<OnEventFinishTask> onFinishTasks = new ArrayList<OnEventFinishTask>();
	
	/**
	 * The delay of the event
	 */
	private int delay = 0;
	
	/**
	 * The last tick
	 */
	private int lastTick = 0;
	
	/**
	 * Does the event repeat?
	 */
	private boolean repeat;
	
	/**
	 * Stops the event
	 */
	public void stop() {
		EventManager.getSingleton().removeEvent(this);
	}
	
	/**
	 * Ran on tick
	 */
	public abstract Event tick();
	
	/**
	 * Adds an EventFinishTask
	 * @param task
	 * @return
	 */
	public Event addOnFinishListener(OnEventFinishTask task) {
		onFinishTasks.add(task);
		return this;
	}

	/**
	 * @param delay the delay to set
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}

	/**
	 * @return the delay
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * @param lastTick the lastTick to set
	 */
	public void setLastTick(int lastTick) {
		this.lastTick = lastTick;
	}

	/**
	 * @return the lastTick
	 */
	public int getLastTick() {
		return lastTick;
	}

	/**
	 * @param repeat the repeat to set
	 */
	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	/**
	 * @return the repeat
	 */
	public boolean isRepeat() {
		return repeat;
	}

}
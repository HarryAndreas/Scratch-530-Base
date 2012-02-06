package org.rs2.event;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class EventManager {
	
	/**
	 * The EventManager instance;
	 */
	private static EventManager instance;
	
	/**
	 * Gets the singleton instance
	 * @return
	 */
	public static EventManager getSingleton() {
		if(instance == null) {
			instance = new EventManager();
		}
		return instance;
	}
	
	/**
	 * Construct the event manager
	 */
	private EventManager() {
		eventsToAdd = new LinkedList<Event>();
		eventsToRemove = new LinkedList<Event>();
		eventsToProcess = new LinkedList<Event>();
	}
	
	/**
	 * A list of events to add on the next tick
	 */
	private final List<Event> eventsToAdd;
	
	/**
	 * A list of events to remove on the next tick
	 */
	private final List<Event> eventsToRemove;
	
	/**
	 * A list of events to process on the next tick
	 */
	private final List<Event> eventsToProcess;
	
	/**
	 * Add an event to process
	 * @param e
	 */
	public void addEvent(Event e) {
		getEventsToAdd().add(e);
	}
	
	/**
	 * Adds an event to remove
	 * @param e
	 */
	public void removeEvent(Event e) {
		getEventsToRemove().add(e);
	}
	
	/**
	 * Sorts out the events
	 */
	private void preTick() {
		if(getEventsToAdd().isEmpty() && getEventsToRemove().isEmpty()) {
			return;
		}
		List<Event> toAdd = Collections.unmodifiableList(getEventsToAdd());
		List<Event> toRemove = Collections.unmodifiableList(getEventsToRemove());
		getEventsToProcess().addAll(toAdd);
		getEventsToProcess().removeAll(toRemove);
		getEventsToAdd().clear();
		getEventsToRemove().clear();
	}
	
	/**
	 * Processes the events
	 */
	public void tick() {
		preTick();
		if(getEventsToProcess().isEmpty())
			return;
		Iterator<Event> it$ = getEventsToProcess().iterator();
		while(it$.hasNext()) {
			Event e = it$.next();
			if(e.getLastTick() >= e.getDelay()) {
				try {
					e.tick();
				} catch(Exception ee) {
					e.stop();
					ee.printStackTrace();
					continue;
				}
				if(!e.onFinishTasks.isEmpty()) {
					for(OnEventFinishTask task : e.onFinishTasks) {
						task.onFinish();
					}
				}
				if(e.isRepeat()) {
					e.setLastTick(0);
				} else {
					e.stop();
				}
			} else {
				e.setLastTick(e.getLastTick() + 1);
			}
		}
	}

	/**
	 * @return the eventsToAdd
	 */
	public List<Event> getEventsToAdd() {
		return eventsToAdd;
	}

	/**
	 * @return the eventsToRemove
	 */
	public List<Event> getEventsToRemove() {
		return eventsToRemove;
	}

	/**
	 * @return the eventsToProcess
	 */
	public List<Event> getEventsToProcess() {
		return eventsToProcess;
	}

}
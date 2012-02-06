package org.rs2.model.item;


/**
 * Container class.
 * @author Graham
 *
 * @param <T>
 */
public class Container<T extends Item> {
	
	private Item[] data;
	private boolean alwaysStackable = false;
	
	public Container(int size, boolean alwaysStackable) {
		data = new Item[size];
		this.alwaysStackable = alwaysStackable;
	}
	
	public void shift() {
		Item[] oldData = data;
		data = new Item[oldData.length];
		int ptr = 0;
		for(int i = 0; i < data.length; i++) {
			if(oldData[i] != null) {
				data[ptr++] = oldData[i];
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public T get(int slot) {
		if(slot < 0 || slot >= data.length) {
			return null;
		}
		return (T) data[slot];
	}
	
	public void set(int slot, T item) {
		if(slot < 0 || slot >= data.length) {
			return;
		}
		data[slot] = item;
	}
	
	public boolean forceAdd(T item) {
		for(int i = 0; i < data.length; i++) {
			if(data[i] == null) {
				data[i] = item;
				return true;
			}
		}
		return false;
	}
	
	public boolean add(T item) {
		if(alwaysStackable || item.getDefinition().isStackable() || item.getDefinition().isNoted()) {
			for(int i = 0; i < data.length; i++) {
				if(data[i] != null) {
					if(data[i].getItemId() == item.getItemId()) {
						data[i] = new Item(data[i].getDefinition().getId(), data[i].getItemAmount() + item.getItemAmount());
						return true;
					}
				}
			}
		} else {
			if(item.getItemAmount() > 1) {
				if(freeSlots() >= item.getItemAmount()) {
					for(int i = 0; i < item.getItemAmount(); i++) {
						int index = freeSlot();
						data[index] = new Item(item.getItemId(), 1);
					}
					return true;
				} else {
					return false;
				}
			}
		}
		int index = freeSlot();
		if(index == -1) {
			return false;
		}
		data[index] = item;
		return true;
	}
	
	public int freeSlots() {
		int j = 0;
		for(int i = 0; i < data.length; i++) {
			if(data[i] == null) {
				j++;
			}
		}
		return j;
	}
	
	public void remove(T item) {
		int removed = 0, toRemove = item.getItemAmount();
		for(int i = 0; i < data.length; i++) {
			if(data[i] != null) {
				if(data[i].getItemId() == item.getItemId()) {
					int amt = data[i].getItemAmount();
					if(amt > toRemove) {
						removed += toRemove;
						amt -= toRemove;
						toRemove = 0;
						data[i] = new Item(data[i].getDefinition().getId(), amt);
						return;
					} else {
						removed += amt;
						toRemove -= amt;
						data[i] = null;
					}
				}
			}
		}
	}
	
	public void removeAll(T item) {
		for(int i = 0; i < data.length; i++) {
			if(data[i] != null) {
				if(data[i].getItemId() == item.getItemId()) {
					data[i] = null;
				}
			}
		}
	}
	
	public boolean containsOne(T item) {
		for(int i = 0; i < data.length; i++) {
			if(data[i] != null) {
				if(data[i].getItemId() == item.getItemId()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean contains(T item) {
		int amtOf = 0;
		for(int i = 0; i < data.length; i++) {
			if(data[i] != null) {
				if(data[i].getItemId() == item.getItemId()) {
					amtOf += data[i].getItemAmount();
				}
			}
		}
		return amtOf >= item.getItemAmount();
	}
	
	public int freeSlot() {
		for(int i = 0; i < data.length; i++) {
			if(data[i] == null) {
				return i;
			}
		}
		return -1;
	}

	public void clear() {
		for(int i = 0; i < data.length; i++) {
			data[i] = null;
		}
	}

	public int getSize() {
		return data.length;
	}

	public int getFreeSlots() {
		int s = 0;
		for(int i = 0; i < data.length; i++) {
			if(data[i] == null) {
				s++;
			}
		}
		return s;
	}

	public int getNumberOf(Item item) {
		int count = 0;
		for(int i = 0; i < data.length; i++) {
			if(data[i] != null) {
				if(data[i].getItemId() == item.getItemId()) {
					count += data[i].getItemAmount();
				}
			}
		}
		return count;
	}

	public Item[] getItems() {
		return data;
	}

	public Container<Item> asItemContainer() {
		Container<Item> c = new Container<Item>(data.length, this.alwaysStackable);
		for(int i = 0; i < data.length; i++) {
			c.data[i] = data[i];
		}
		return c;
	}

	public int getFreeSlot() {
		for(int i = 0; i < data.length; i++) {
			if(data[i] == null) {
				return i;
			}
		}
		return 0;
	}

	public Item lookup(int id) {
		for(int i = 0; i < data.length; i++) {
			if(data[i] == null) {
				continue;
			}
			if(data[i].getItemId() == id) {
				return data[i];
			}
		}
		return null;
	}
	
	public int lookupSlot(int id) {
		for(int i = 0; i < data.length; i++) {
			if(data[i] == null) {
				continue;
			}
			if(data[i].getItemId() == id) {
				return i;
			}
		}
		return -1;
	}

	public void reset() {
		data = new Item[data.length];
	}

	public void remove(int preferredSlot, Item item) {
		int removed = 0, toRemove = item.getItemAmount();
		if(data[preferredSlot] != null) {
			if(data[preferredSlot].getItemId() == item.getItemId()) {
				int amt = data[preferredSlot].getItemAmount();
				if(amt > toRemove) {
					removed += toRemove;
					amt -= toRemove;
					toRemove = 0;
					data[preferredSlot] = new Item(data[preferredSlot].getDefinition().getId(), amt);
					return;
				} else {
					removed += amt;
					toRemove -= amt;
					data[preferredSlot] = null;
				}
			}
		}
		for(int i = 0; i < data.length; i++) {
			if(data[i] != null) {
				if(data[i].getItemId() == item.getItemId()) {
					int amt = data[i].getItemAmount();
					if(amt > toRemove) {
						removed += toRemove;
						amt -= toRemove;
						toRemove = 0;
						data[i] = new Item(data[i].getDefinition().getId(), amt);
						return;
					} else {
						removed += amt;
						toRemove -= amt;
						data[i] = null;
					}
				}
			}
		}
	}

	public void addAll(Container<T> container) {
		for(int i = 0; i < container.getSize(); i++) {
			T item = container.get(i);
			if(item != null) {
				this.add(item);
			}
		}
	}

	public boolean hasSpaceFor(Container<T> container) {
		for(int i = 0; i < container.getSize(); i++) {
			T item = container.get(i);
			if(item != null) {
				if(!this.hasSpaceForItem(item)) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean hasSpaceForItem(T item) {
		if(alwaysStackable || item.getDefinition().isStackable() || item.getDefinition().isNoted()) {
			for(int i = 0; i < data.length; i++) {
				if(data[i] != null) {
					if(data[i].getItemId() == item.getItemId()) {
						return true;
					}
				}
			}
		} else {
			if(item.getItemAmount() > 1) {
				if(freeSlots() >= item.getItemAmount()) {
					return true;
				} else {
					return false;
				}
			}
		}
		int index = freeSlot();
		if(index == -1) {
			return false;
		}
		return true;
	}

}
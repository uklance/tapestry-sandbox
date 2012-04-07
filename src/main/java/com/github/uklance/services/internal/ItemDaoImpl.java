package com.github.uklance.services.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.github.uklance.domain.Item;
import com.github.uklance.services.ItemDao;

public class ItemDaoImpl implements ItemDao {
	private Map<Long, Item> items = new HashMap<Long, Item>();
	private AtomicLong nextId = new AtomicLong(1);

	/* (non-Javadoc)
	 * @see com.github.uklance.services.internal.ItemDao#addItem(com.github.uklance.domain.Item)
	 */
	public void addItem(Item item) {
		item.setItemId(nextId.getAndIncrement());
		items.put(item.getItemId(), item);
	}
	
	/* (non-Javadoc)
	 * @see com.github.uklance.services.internal.ItemDao#getItems(java.util.List)
	 */
	public List<Item> getItems(List<Long> itemIds) {
		List<Item> items = new ArrayList<Item>(itemIds.size());
		for (Long itemId : itemIds) {
			items.add(getItem(itemId));
		}
		return items;
	}
	
	/* (non-Javadoc)
	 * @see com.github.uklance.services.internal.ItemDao#getItem(java.lang.Long)
	 */
	public Item getItem(Long itemId) {
		Item item = items.get(itemId);
		if (item == null) {
			throw new IllegalArgumentException("Item " + itemId);
		}
		return item;
	}
}

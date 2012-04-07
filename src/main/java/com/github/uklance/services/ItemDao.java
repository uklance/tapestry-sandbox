package com.github.uklance.services;

import java.util.List;

import com.github.uklance.domain.Item;

public interface ItemDao {

	public abstract void addItem(Item item);

	public abstract List<Item> getItems(List<Long> itemIds);

	public abstract Item getItem(Long itemId);

}
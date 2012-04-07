package com.github.uklance.services;

import java.util.List;

import com.github.uklance.domain.Item;
import com.github.uklance.domain.ScoredItem;

public interface ItemService {

	public abstract List<ScoredItem> findScoredItems(ItemFilter filter);

	public abstract void addItem(Item item);

}
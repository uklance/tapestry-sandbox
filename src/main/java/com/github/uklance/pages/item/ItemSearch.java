package com.github.uklance.pages.item;

import java.util.List;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.github.uklance.domain.ScoredItem;
import com.github.uklance.services.ItemFilter;
import com.github.uklance.services.ItemService;

public class ItemSearch {
	@Inject
	private ItemService itemService;
	
	@Property
	@Persist(PersistenceConstants.FLASH)
	private ItemFilter filter;
	
	@Property
	@Persist(PersistenceConstants.FLASH)
	private List<ScoredItem> items;
	
	@Property
	private ScoredItem item;

	void onPrepare() {
		if (filter == null) {
			filter = new ItemFilter();
			filter.setLatitude(51.517598273919);
			filter.setLongitude(-0.082235212309326);
		}
	}
	
	void onSuccess() {
		items = itemService.findByFilter(filter);
	}
}

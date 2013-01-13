package com.github.uklance.services.internal;

import java.util.List;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import com.github.uklance.domain.ScoredItem;
import com.github.uklance.services.ItemFilter;

public class ItemServiceImplTest {
	// private ItemCategory TUBE_CATEGORY = new ItemCategory("Tube Station");

	@Test
	public void test() throws Exception {
		ItemServiceImpl service = new ItemServiceImpl(new RAMDirectory(), new ItemDaoImpl());
		new TestItemInserter(service).addTubeStations();

		double liverpoolStLat = 51.517598273919;
		double liverpoolStLng = -0.082235212309326;
		
		ItemFilter filter = new ItemFilter();
		filter.setLatitude(liverpoolStLat);
		filter.setLongitude(liverpoolStLng);
		for (int kms = 1; kms < 5; ++ kms) {
			filter.setKms((double) kms);
			List<ScoredItem> scoredItems = service.findByFilter(filter);
			
			System.out.println(String.format("%s kms = %s ----------------------------------------", kms, scoredItems.size()));
			for (ScoredItem scoredItem : scoredItems) {
				System.out.println(String.format("%s %s", scoredItem.getScore(), scoredItem.getItem().getName()));
			}
		}
		
	}
}

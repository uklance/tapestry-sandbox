package com.github.uklance.services.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.apache.lucene.spatial.query.SpatialArgs;
import org.apache.lucene.spatial.query.SpatialOperation;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import com.github.uklance.domain.Item;
import com.github.uklance.domain.ScoredItem;
import com.github.uklance.services.ItemDao;
import com.github.uklance.services.ItemFilter;
import com.github.uklance.services.ItemService;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.shape.Circle;
import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.Shape;

public class ItemServiceImpl implements ItemService {
	private Directory directory;
	private ItemDao itemDao;
	
	private Version LUCENE_VERSION = Version.LUCENE_40;

	private SpatialContext ctx = SpatialContext.GEO;
	private int maxLevels = 11;
	// This can also be constructed from SpatialPrefixTreeFactory
	private SpatialPrefixTree grid = new GeohashPrefixTree(ctx, maxLevels);
	private SpatialStrategy strategy = new RecursivePrefixTreeStrategy(grid, "location");

	public ItemServiceImpl(Directory directory, ItemDao itemDao) {
		super();
		this.directory = directory;
		this.itemDao = itemDao;
	}

	public List<ScoredItem> findByFilter(ItemFilter filter) {
		Map<Long, Double> itemIdDistances = findItemIdDistances(filter);
		List<Item> items = itemDao.getItems(new ArrayList<Long>(itemIdDistances.keySet()));
		List<ScoredItem> scoredItems = new ArrayList<ScoredItem>(items.size());
		for (Item item : items) {
			Double score = itemIdDistances.get(item.getItemId());
			ScoredItem scoredItem = new ScoredItem(item, score);
			scoredItems.add(scoredItem);
		}
		Collections.sort(scoredItems);
		return scoredItems;
	}

	protected Map<Long, Double> findItemIdDistances(ItemFilter itemFilter) {
		try {
			IndexReader indexReader = DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			Point point = ctx.makePoint(itemFilter.getLongitude(), itemFilter.getLatitude());
			ValueSource valueSource = strategy.makeDistanceValueSource(point);
			Sort distanceSort = new Sort(valueSource.getSortField(false)).rewrite(indexSearcher);

			double degrees = DistanceUtils.dist2Degrees(itemFilter.getKms(), DistanceUtils.EARTH_MEAN_RADIUS_KM);
			Circle circle = ctx.makeCircle(point, degrees);
			SpatialArgs args = new SpatialArgs(SpatialOperation.Intersects, circle);
			Filter filter = strategy.makeFilter(args);
			TopDocs docs = indexSearcher.search(new MatchAllDocsQuery(), filter, Integer.MAX_VALUE, distanceSort);
			
			Map<Long, Double> scoresByItemId = new HashMap<Long, Double>();
			for (ScoreDoc scoreDoc : docs.scoreDocs) {
				Document doc = indexSearcher.doc(scoreDoc.doc);
				Long itemId = doc.getField("itemId").numericValue().longValue();
				String pointString = doc.getField(strategy.getFieldName()).stringValue();
				Point docPoint = (Point) ctx.readShape(pointString);
				double distDegrees = ctx.getDistCalc().distance(point, docPoint);
				double distKm = DistanceUtils.degrees2Dist(distDegrees, DistanceUtils.EARTH_MEAN_RADIUS_KM);
				scoresByItemId.put(itemId, distKm);
			}
			return scoresByItemId;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void addItems(Item... items) {
		try {
			IndexWriterConfig iwConfig = new IndexWriterConfig(LUCENE_VERSION, null);
			IndexWriter indexWriter = new IndexWriter(directory, iwConfig);
			for (Item item : items) {
				itemDao.addItem(item);

				Document doc = new Document();
				doc.add(new LongField("itemId", item.getItemId(), Field.Store.YES));
				Shape shape = ctx.makePoint(item.getLongitude(), item.getLatitude());
				for (IndexableField f : strategy.createIndexableFields(shape)) {
					doc.add(f);
				}
				// store it too; the format is up to you
				doc.add(new StoredField(strategy.getFieldName(), ctx.toString(shape)));
				
				indexWriter.addDocument(doc);
			}
			indexWriter.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

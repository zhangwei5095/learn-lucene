package xyz.anduo.lucene;

import xyz.anduo.lucene.day02.Search;
import junit.framework.TestCase;

public class SearchTest extends TestCase {

	public void testIndexSearch() {
	Search search = new Search();
	search.indexSearch("content", "Yesterday");
	}
	

}

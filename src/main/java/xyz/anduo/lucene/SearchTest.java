package xyz.anduo.lucene;

import junit.framework.TestCase;

public class SearchTest extends TestCase {

	public void testIndexSearch() {
	Search search = new Search();
	search.indexSearch("content", "Yesterday");
	}
	

}

package xyz.anduo.lucene;

import junit.framework.TestCase;

public class HelloLuceneTest extends TestCase {

	public void testIndex() {
		HelloLucene lucene = new HelloLucene();
		lucene.index();
	}

	public void testSearch() {
		HelloLucene lucene = new HelloLucene();
		lucene.search();
	}

}

package xyz.anduo.lucene;

import junit.framework.TestCase;

public class IndexProcessTest extends TestCase {

	public void testCreateIndex() {
		IndexProcess indexProcess = new IndexProcess();
		indexProcess.createIndex("d:\\testdir\\");
	}
	

}

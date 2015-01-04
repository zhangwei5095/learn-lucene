package xyz.anduo.lucene.day02;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.FSDirectory;

public class Search {
	private String INDEX_STORE_PATH = "d:\\index";

	// 利用lucene的搜索
	public void indexSearch(String searchType, String searchKey) {
		try {
			// 根据索引位置建立IndexSearcher
			IndexSearcher searcher = new IndexSearcher(IndexReader.open(FSDirectory.open(new File(INDEX_STORE_PATH))));
			
			// 建立搜索单元，searchType代表搜索的Field，searchKey代表关键字
			Term t = new Term(searchType, searchKey);

			// 有Term生成一个Query
			Query q = new TermQuery(t);

			// 获取<document,frequency>的枚举对象
			TermDocs termDocs = searcher.getIndexReader().termDocs(t);

			while (termDocs.next()) {

				// 输出在文档中出现关键词的次数
				System.out.println("find " + termDocs.freq() + " matches in ");

				// 搜索到关键字的文档
				System.out.println(searcher.getIndexReader().document(termDocs.doc()).getField("filename")
						.stringValue());
			}

		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

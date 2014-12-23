package xyz.anduo.lucene;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * lucene的第一个例子
 * 
 * @author anduo
 * 
 */
public class HelloLucene {

	/**
	 * 1、建立索引
	 */
	public void index() {
		
		IndexWriter indexWriter = null;
		try {
			// 1、创建 Directory
			//Directory directory = new RAMDirectory();//建立内存索引
			Directory directory = FSDirectory.open(new File("/home/anduo/workspace/lucene_learn/indexes"));//创建在硬盘上
			
			// 2、创建 IndexWriter
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_35, analyzer);
			indexWriter = new IndexWriter(directory, indexWriterConfig);
			// 3、创建 Document 对象
			Document document = null;
			// 4、为Document添加Field
			File fileList = new File("/home/anduo/workspace/lucene/files");
			for (File file : fileList.listFiles()) {
				document = new Document();
				document.add(new Field("content", new FileReader(file)));
				document.add(new Field("filename", file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				document.add(new Field("path", file.getAbsolutePath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				// 5、通过IdexUriter添加文档到索引中
				indexWriter.addDocument(document);
			}
			
			
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (indexWriter != null)
				try {
					indexWriter.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}
	
	/**
	 * 搜索
	 */
	public void search(){
		try {
		//1、创建directory
		Directory directory = FSDirectory.open(new File("/home/anduo/workspace/lucene_learn/indexes"));
		//2、创建IndexReader
		IndexReader indexReader =IndexReader.open(directory);
		//3、根据IndexReader创建IndexSearcher
		@SuppressWarnings("resource")
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		//4、创建Query
		QueryParser queryParser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
		Query query = queryParser.parse("Apache");
		//5、根据searcher搜索并返回对象TopDocs
		TopDocs topDocs = indexSearcher.search(query, 10);
		//6、根据TopDocs获取ScoreDoc
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		//7、根据searcher和ScoreDoc获取具体的document
		for (ScoreDoc scoreDoc : scoreDocs) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			System.out.println("filename:"+ document.get("filename")+";path:"+document.get("path"));
		}
		//8、根据document获取需要的值
		
		}catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}

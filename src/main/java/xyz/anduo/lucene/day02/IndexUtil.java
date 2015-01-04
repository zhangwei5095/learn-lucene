package xyz.anduo.lucene.day02;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class IndexUtil {
	private String[] ids = { "1", "2", "3", "4", "5", "6" };
	private String[] emails = { "aa@qq.com", "bb@qq.com", "cc@qq.com", "ee@qq.com", "ff@qq.com", "gg@qq.com" };
	private String[] contents = {
			"In short words, install Node.js with needed packages, Java and Eclipse package like Enide Studio:",
			"may require to use x32 version for Java 6-7 (But for Java 8 seems OK, more feedback about different OS needed). x32 bit JDK would be good enough for using IDE. You can use x64 bit JDK, if you are OK with #71 Anyway Java software stack should be of the same bit length x64 or x32 (e.g. Windows x64, JDK x64, Eclipse x64; or Windows x64, JDK x32, Eclipse x32). ",
			"Now you are ready to develop Node.js applications with Nodeclipse!",
			"Start Eclipse, then select Help > Install New Software...",
			"After a while depending of what you had and what you selected,you should see the center box filled with Eclipse plugins to install.Click Next.",
			"Switch to Node perspective (e.g. Window -> Open Perspective -> Other ... -> Node)." };
	private int[] attachs = {1,2,3,4,2,6};
	private String[] names = {"zhangsan","lisi","wangwu","james","kobi","jake"};

	private Directory directory = null;
	
	public void index(){
		IndexWriter indexWriter = null;
		try {
			
			//1.创建directory
			directory = FSDirectory.open(new File("/home/anduo/workspace/lucene_learn/indexes"));
			//2.创建IndexWriter
			indexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			
			//3.创建Document
			Document  doc = null;
			for (int i = 0; i < ids.length; i++) {
				// 4、为Document添加Field
				
				doc = new Document();
				//Field.Store.*  [存储域选项]
				//YES,表示会把这个域中的内容完全存储在索引文件中，方便进行文本的还原
				//NO,表示这个域的内容不存储在索引文件中，但是可以被索引，此时内容无法进行还原(doc.get方法)
				
				//Field.Index.*  [索引域选项]
				//ANALYZED,进行分词和索引，使用于标题、内容等
				//NOT_ANALYZED,进行索引，但是不进行分词，如身份证号、姓名、ID等，适用精准搜索
				//ANALYZED_NOT_NORMS,进行分词但是不存储norms信息，这个norms中包含了创建索引的时间和权值等信息
				//NOT_ANALYZED_NOT_NORMS,既不分词也不索引
				//No,完全不索引，不存储
				doc.add(new Field("id", ids[i], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
				doc.add(new Field("email", emails[i], Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("content", contents[i],Field.Store.NO,Field.Index.ANALYZED));
				doc.add(new Field("name", names[i], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
				
				// 5、通过IdexUriter添加文档到索引中
				indexWriter.addDocument(doc);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

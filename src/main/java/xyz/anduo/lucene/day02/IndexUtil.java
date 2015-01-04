package xyz.anduo.lucene.day02;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
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
	private int[] attachs = { 1, 2, 3, 4, 2, 6 };
	private String[] names = { "zhangsan", "lisi", "wangwu", "james", "kobi", "jake" };

	private Directory directory = null;

	public void index() {
		IndexWriter indexWriter = null;
		try {

			// 1.创建directory
			directory = FSDirectory.open(new File("/home/anduo/workspace/lucene_learn/indexes"));
			// 2.创建IndexWriter
			indexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(
					Version.LUCENE_35)));

			// 3.创建Document
			Document doc = null;
			for (int i = 0; i < ids.length; i++) {
				// 4、为Document添加Field

				doc = new Document();
				// Field.Store.* [存储域选项]
				// YES,表示会把这个域中的内容完全存储在索引文件中，方便进行文本的还原
				// NO,表示这个域的内容不存储在索引文件中，但是可以被索引，此时内容无法进行还原(doc.get方法)

				// Field.Index.* [索引域选项]
				// ANALYZED,进行分词和索引，使用于标题、内容等
				// NOT_ANALYZED,进行索引，但是不进行分词，如身份证号、姓名、ID等，适用精准搜索
				// ANALYZED_NOT_NORMS,进行分词但是不存储norms信息，这个norms中包含了创建索引的时间和权值等信息
				// NOT_ANALYZED_NOT_NORMS,既不分词也不索引
				// No,完全不索引，不存储
				doc.add(new Field("id", ids[i], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
				doc.add(new Field("email", emails[i], Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("content", contents[i], Field.Store.NO, Field.Index.ANALYZED));
				doc.add(new Field("name", names[i], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));

				// 5、通过IdexUriter添加文档到索引中
				indexWriter.addDocument(doc);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 手动索引优化
	 */
	public void merge() {
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(
					Version.LUCENE_35)));
			// 会将索引强制合并为两段，这两段被删除的数据会被清空，此处lunene在3.5之后不建议使用，因为会消耗大量的内存开销，lunene会自动处理
			writer.forceMerge(2);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * 删除回收站
	 */
	public void forceDelete() {
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(
					Version.LUCENE_35)));
			writer.forceMergeDeletes();

		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * 恢复索引
	 */
	public void undelete() {
		IndexReader reader = null;
		try {
			reader = IndexReader.open(directory, false);
			// 恢复时，必须把indexreader的只读(readonly)设为false
			reader.undeleteAll();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * 删除索引
	 */

	public void delete() {
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(
					Version.LUCENE_35)));
			// 参数是一个选项，参数可以为一个Query，也可以是一个Term（精确查找的值）
			// 此删除的文档并不是完全删除，而是存在一个回收站中，可以恢复
			// 删除ID为1的文档
			writer.deleteDocuments(new Term("id", "1"));

		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

	/**
	 * 更新索引
	 */
	public void update() {
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(
					Version.LUCENE_35)));
			/**
			 * lunene其实并没有提供更新，在这里更新操作其实是如下两个操作:先删除，再添加
			 */
			Document doc = new Document();
			doc.add(new Field("id", ids[0], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
			doc.add(new Field("email", emails[0], Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("content", contents[0], Field.Store.NO, Field.Index.ANALYZED));
			doc.add(new Field("name", names[0], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
			writer.updateDocument(new Term("id", "1"), doc);
			
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * 查询
	 */
	public void query() {
		IndexReader reader = null;
		try {
			reader = IndexReader.open(directory);
			// 通过reader可以有效的获取到文档的数量
			System.out.println("numDocs:" + reader.numDocs());
			System.out.println("maxDocs:" + reader.maxDoc());
			System.out.println("deleteDocs:" + reader.numDeletedDocs());
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}

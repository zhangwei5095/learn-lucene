package xyz.anduo.lucene.day02;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * 索引创建进程
 * 
 * @author anduo
 * 
 */
public class IndexProcess {
	/**
	 * 存储创建索引文件的位置
	 */
	private String INDEX_STORE_PATH = "d:\\index";

	public void createIndex(String inputDir) {
		try {
			// 构建一个 IndexWriter
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
			IndexWriter writer = new IndexWriter(FSDirectory.open(new File(INDEX_STORE_PATH)), config);

			// 获取所有文件所需要建立索引文件的文件数组
			File filesDir = new File(inputDir);
			File[] files = filesDir.listFiles();
			for (File file : files) {
				String fileName = file.getName();
				// 判断文件是否为txt类型的文件
				if (fileName.substring(fileName.lastIndexOf(".")).equals(".txt")) {
					// 创建一个Document对象
					Document doc = new Document();

					// 为文件名创建一个 Field
					Field field = new Field("filename", file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED);
					doc.add(field);

					// 为内容创建一个 Field
					field = new Field("content", loadFileToString(file), Field.Store.NO, Field.Index.ANALYZED_NO_NORMS);
					doc.add(field);

					// 将Document加入IndexWriter
					writer.addDocument(doc);
				}
			}

			if (writer != null) {
				writer.close();
			}

		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将文件中的内容读出来，所有的内容放到一个String中返回
	 * 
	 * @param file
	 * @return
	 */
	private String loadFileToString(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuffer sb = new StringBuffer();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			br.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}

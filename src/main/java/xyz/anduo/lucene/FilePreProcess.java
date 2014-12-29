package xyz.anduo.lucene;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 处理文件，将大文件处理小文件
 * 
 * @author anduo
 * 
 */
public class FilePreProcess {

	/**
	 * 
	 * @param file
	 *            要被处理的源文件
	 * @param outputDir
	 *            处理后的文件输出路径
	 */
	public static void preprocess(File file, String outputDir) {
		try {
			splitToSmallFiles(file, outputDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 对文件字符进行全角/半角处理
	 * 
	 * @param file
	 * @param destFile
	 * @return
	 * @throws IOException
	 */
	private static File charactorProcess(File file, String destFile) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(destFile));
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		while (line != null) {
			if (!line.equals("\r\n")) {
				//String newLine = replace(line);
				writer.write(line);
				writer.newLine();
			}
		}
		reader.close();
		writer.close();
		return new File(destFile);
	}

	/**
	 * 拆分小文件
	 * 
	 * @param file
	 * @param outputpath
	 * @throws IOException
	 */
	private static void splitToSmallFiles(File file, String outputpath) throws IOException {
		int filePointer = 0;
		int MAX_SIZE = 1024;

		BufferedWriter writer = null;
		BufferedReader reader = new BufferedReader(new FileReader(file));
		StringBuffer buffer = new StringBuffer();
		String line = reader.readLine();

		while (line != null) {
			buffer.append(line).append("\r\n");
			if (buffer.toString().getBytes().length > MAX_SIZE) {
				writer = new BufferedWriter(new FileWriter(outputpath + "output" + filePointer + ".txt"));
				writer.write(buffer.toString());
				writer.close();
				filePointer++;
				buffer = new StringBuffer();
			}
			line = reader.readLine();
		}
		writer = new BufferedWriter(new FileWriter(outputpath + "output" + filePointer + ".txt"));
		writer.write(buffer.toString());
		reader.close();
		writer.close();
	}

	/**
	 * 全角空格为12288，半角空格为32 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
	 * 
	 * 将字符串中的全角字符转为半角
	 * 
	 * @param line 要转换的包含全角的任意字符串
	 * @return 转换之后的字符串
	 */
	private static String replace(String line) {
		char[] c = line.toCharArray();
		for (int index = 0; index < c.length; index++) {
			if (c[index] == 12288) {// 全角空格
				c[index] = (char) 32;
			} else if (c[index] > 65280 && c[index] < 65375) {// 其他全角字符
				c[index] = (char) (c[index] - 65248);
			}
		}
		return String.valueOf(c);
	}

}

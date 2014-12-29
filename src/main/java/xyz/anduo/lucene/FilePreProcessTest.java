package xyz.anduo.lucene;

import java.io.File;

import junit.framework.TestCase;

public class FilePreProcessTest extends TestCase {

	public void testPreprocess() {
		String inputFile ="d:\\test.txt";
		
		String outputDir = "d:\\testdir\\";
		
		if(!new File(outputDir).exists()){
			new File(outputDir).mkdir();
		}
		
		FilePreProcess.preprocess(new File(inputFile), outputDir);
		
	}

	

}

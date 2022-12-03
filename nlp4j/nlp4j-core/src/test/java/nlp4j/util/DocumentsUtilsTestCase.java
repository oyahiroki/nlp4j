package nlp4j.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.DocumentBuilder;

public class DocumentsUtilsTestCase extends TestCase {

	public void testPrintAsCsv001() throws IOException {
		List<Document> docs = new ArrayList<>();

		docs.add((new DocumentBuilder()).put("aaa", "111").put("bbb", "222").put("ccc", "333").create());
		docs.add((new DocumentBuilder()).put("aaa", "111").put("bbb", "222").put("ccc", "333").create());
		docs.add((new DocumentBuilder()).put("aaa", "111").put("bbb", "222").put("xxx", "333").create());

		DocumentsUtils.printAsCsv(docs, System.out);

	}

	public void testPrintAsCsv002() throws IOException {
		List<Document> docs = new ArrayList<>();

		docs.add((new DocumentBuilder()).put("aaa", "111").put("bbb", "222").put("ccc", "333").create());
		docs.add((new DocumentBuilder()).put("aaa", "111").put("bbb", "222").put("ccc", "333").create());
		docs.add((new DocumentBuilder()).put("aaa", "111").put("bbb", "222").put("xxx", "333").create());

		File temp = File.createTempFile("test", ".csv");
		System.err.println(temp.getAbsolutePath());
		DocumentsUtils.printAsCsv(docs, temp, "UTF-8");

	}

}

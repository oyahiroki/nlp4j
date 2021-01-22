package nlp4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

public class DocumentProcessorTestCase extends TestCase {

	static Class target = DocumentProcessor.class;

	public void testDocumentProcessor001() throws Exception {
		String prop = "";
		Properties props = new Properties();
		props.load(new ByteArrayInputStream(prop.getBytes("utf-8")));
		DocumentProcessor dp = new DocumentProcessor(props);
	}

	public void testDocumentProcessor002() throws Exception {
		String prop = "" //
				+ "crawler[0].name=nlp4j.CrawlerForTest" + "\n" //
				+ "annotator[0].name=nlp4j.AnnotatorForTest" + "\n" //
				+ "importer[0].name=nlp4j.DocumentImporterForTest" + "\n" //
				+ "";
		Properties props = new Properties();
		props.load(new ByteArrayInputStream(prop.getBytes("utf-8")));
		DocumentProcessor dp = new DocumentProcessor(props);
	}

	public void testGetDocs001() {
	}

	public void testCrawlDocuments001() throws Exception {
		String prop = "" //
				+ "crawler[0].name=nlp4j.CrawlerForTest" + "\n" //
				+ "annotator[0].name=nlp4j.AnnotatorForTest" + "\n" //
				+ "importer[0].name=nlp4j.DocumentImporterForTest" + "\n" //
				+ "";
		Properties props = new Properties();
		props.load(new ByteArrayInputStream(prop.getBytes("utf-8")));
		DocumentProcessor dp = new DocumentProcessor(props);

		List<Document> docs = dp.crawlDocuments();
		for (Document doc : docs) {
			System.err.println(doc);
		}
	}

	public void testAnnotateDocuments001() throws Exception {
		String prop = "" //
				+ "crawler[0].name=nlp4j.CrawlerForTest" + "\n" //
				+ "annotator[0].name=nlp4j.AnnotatorForTest" + "\n" //
				+ "importer[0].name=nlp4j.DocumentImporterForTest" + "\n" //
				+ "";
		Properties props = new Properties();
		props.load(new ByteArrayInputStream(prop.getBytes("utf-8")));
		DocumentProcessor dp = new DocumentProcessor(props);

		dp.crawlDocuments();
		List<Document> docs = dp.annotateDocuments();

		for (Document doc : docs) {
			System.err.println(doc);
		}

	}

	public void testImportDocuments001() throws Exception {
		String prop = "" //
				+ "crawler[0].name=nlp4j.CrawlerForTest" + "\n" //
				+ "annotator[0].name=nlp4j.AnnotatorForTest" + "\n" //
				+ "importer[0].name=nlp4j.DocumentImporterForTest" + "\n" //
				+ "";
		Properties props = new Properties();
		props.load(new ByteArrayInputStream(prop.getBytes("utf-8")));
		DocumentProcessor dp = new DocumentProcessor(props);

		dp.crawlDocuments();
		dp.annotateDocuments();
		dp.importDocuments();

	}

}

package nlp4j.annotator.concurrent;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentBuilder;
import nlp4j.annotator.DebugAnnotator;

public class ConcurrentDocumentAnnotatorTestCase extends TestCase {

	public void testAnnotateListOfDocument001() throws Exception {
		List<DocumentAnnotator> anns = new ArrayList<>();
		{
			for (int n = 0; n < 10; n++) {
				DebugAnnotator ann = new DebugAnnotator();
				ann.setProperty("wait", "1000");
				anns.add(ann);
			}
		}
		// 並列処理に利用するAnnotator
		try (ConcurrentDocumentAnnotator ca = new ConcurrentDocumentAnnotator(anns);) {
			List<Document> docs = new ArrayList<>();
			for (int n = 0; n < 10; n++) {
				Document doc = (new DocumentBuilder()).text("This is test " + n).build();
				docs.add(doc);
			}
			long time1 = System.currentTimeMillis();
			ca.annotate(docs);
			long time2 = System.currentTimeMillis();
			System.err.println("time: " + (time2 - time1));

			long totaltime = (time2 - time1);
			assertTrue(totaltime < 1000 * 2); // 並列の効果が確認できること
		}

	}

	public void testAnnotateListOfDocument002() throws Exception {
		List<DocumentAnnotator> anns = new ArrayList<>();
		{
			for (int n = 0; n < 3; n++) {
				DebugAnnotator ann = new DebugAnnotator();
				ann.setProperty("wait", "1000");
				anns.add(ann);
			}
		}
		// 並列処理に利用するAnnotator
		try (ConcurrentDocumentAnnotator ca = new ConcurrentDocumentAnnotator(anns);) {
			List<Document> docs = new ArrayList<>();
			for (int n = 0; n < 10; n++) {
				Document doc = (new DocumentBuilder()).text("This is test " + n).build();
				docs.add(doc);
			}
			long time1 = System.currentTimeMillis();
			ca.annotate(docs);
			long time2 = System.currentTimeMillis();
			System.err.println("time: " + (time2 - time1));

			long totaltime = (time2 - time1);
			assertTrue(totaltime < (1000 * 10)); // 並列の効果が確認できること
			assertTrue((1000 * 4) < totaltime); // 並列の効果が確認できること
		}

	}
}

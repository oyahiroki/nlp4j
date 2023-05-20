package nlp4j.ginza;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentBuilder;
import nlp4j.annotator.concurrent.ConcurrentDocumentAnnotator;

public class GinzaPosDependencyAnnotatorConcurrentTestCase extends TestCase {

	public void test001() throws Exception {

		List<DocumentAnnotator> anns = new ArrayList<>();
		{
			{
				// 形態素解析＋構文解析 1
				GinzaPosDependencyAnnotator annGinza = new GinzaPosDependencyAnnotator();
				annGinza.setProperty("target", "text");
				annGinza.setProperty("endPoint", "http://127.0.0.1:8888");
				anns.add(annGinza);
			}
			{
				// 形態素解析＋構文解析 2
				GinzaPosDependencyAnnotator annGinza = new GinzaPosDependencyAnnotator();
				annGinza.setProperty("target", "text");
				annGinza.setProperty("endPoint", "http://127.0.0.1:8889");
				anns.add(annGinza);
			}
			{
				// 形態素解析＋構文解析 3
				GinzaPosDependencyAnnotator annGinza = new GinzaPosDependencyAnnotator();
				annGinza.setProperty("target", "text");
				annGinza.setProperty("endPoint", "http://127.0.0.1:8890");
				anns.add(annGinza);
			}
//			{
//				// 形態素解析＋構文解析 4
//				GinzaPosDependencyAnnotator annGinza = new GinzaPosDependencyAnnotator();
//				annGinza.setProperty("target", "text");
//				annGinza.setProperty("endPoint", "http://127.0.0.1:8891");
//				anns.add(annGinza);
//			}
		}

		ConcurrentDocumentAnnotator ca = new ConcurrentDocumentAnnotator(anns);

		List<Document> docs = new ArrayList<>();

		for (int n = 0; n < 10; n++) {
			Document doc = (new DocumentBuilder()).text((n + 1) + "日はいい天気です ").build();
			docs.add(doc);
		}

		ca.annotate(docs);

		for (Document doc : docs) {
			System.err.println(doc.toString());
		}
	}

}

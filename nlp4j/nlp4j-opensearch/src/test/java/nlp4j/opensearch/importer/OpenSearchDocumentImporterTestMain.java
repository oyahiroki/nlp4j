package nlp4j.opensearch.importer;

import nlp4j.Document;
import nlp4j.DocumentBuilder;
import nlp4j.opensearch.annotator.OpenSearchAnalyzerAnnotator;

public class OpenSearchDocumentImporterTestMain {

	public static void main(String[] args) throws Exception {
		final String index = "hello-opensearch";
		Document doc = (new DocumentBuilder()).text("これはテストです。").build();

		{
			OpenSearchAnalyzerAnnotator ann = new OpenSearchAnalyzerAnnotator();
			ann.setProperty("index", index);
			ann.setProperty("target", "text");
			ann.annotate(doc);
		}

		OpenSearchDocumentImporter ipt = new OpenSearchDocumentImporter();
		{
			ipt.setProperty("index", index);
		}
		ipt.importDocument(doc);
		ipt.close();

	}

}

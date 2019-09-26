package example;

import java.util.ArrayList;
import java.util.List;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentAnnotatorPipeline;
import nlp4j.DocumentImporter;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultDocumentAnnotator;
import nlp4j.impl.DefaultDocumentAnnotatorPipeline;
import nlp4j.impl.DefaultDocumentImporter;
import nlp4j.yhoo_jp.YJpMaAnnotator;

public class DocumentProcessExample1Main {

	public static void main(String[] args) throws Exception {

		List<Document> docs = new ArrayList<>();
		{
			Document doc = new DefaultDocument();
			doc.setText("これはテストです。");
			docs.add(doc);
		}

		System.err.println(docs);

		DocumentAnnotatorPipeline pipeline = new DefaultDocumentAnnotatorPipeline();
		{
			DocumentAnnotator annotator = new DefaultDocumentAnnotator();
			pipeline.add(annotator);
		}
		{
			DocumentAnnotator annotator = new YJpMaAnnotator();
			pipeline.add(annotator);
		}

		pipeline.annotate(docs);

		System.err.println(docs);
		for (Document doc : docs) {
			for (Keyword kwd : doc.getKeywords()) {
				System.err.println("facet=" + kwd.getFacet() + ",lex=" + kwd.getLex());
			}
		}

		DocumentImporter importer = new DefaultDocumentImporter();
		{
			importer.importDocuments(docs);
		}

	}

}

package test;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Callable;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.mecab.MecabAnnotator;

public class RunnableNLP implements  Callable<Document>, Closeable {

	MecabAnnotator annotator;

	String text;

	public RunnableNLP(String text) {
		super();
		annotator = new MecabAnnotator();
		this.text = text;
	}


	@Override
	public Document call() throws Exception {
		try {
			Document doc = new DefaultDocument();
			doc.putAttribute("text", text);
			annotator.setProperty("target", "text");
			annotator.annotate(doc); // throws Exception
			System.err.println("Finished : annotation");

			System.err.println(doc.getKeywords().size());

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void close() throws IOException {
		if (annotator != null) {
			annotator.close();
		}
	}

}

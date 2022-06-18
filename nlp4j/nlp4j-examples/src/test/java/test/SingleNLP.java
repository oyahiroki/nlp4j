package test;

import java.util.List;

import nlp4j.Document;
import nlp4j.crawler.TextFileLineSeparatedCrawler;
import nlp4j.mecab.MecabAnnotator;

public class SingleNLP {

	public static void main(String[] args) throws Exception {

		TextFileLineSeparatedCrawler crawler = new TextFileLineSeparatedCrawler();
		crawler.setProperty("file", "file/train-v1.0-question-head1000.txt");

		MecabAnnotator annMecab = new MecabAnnotator();

		long time1 = System.currentTimeMillis();

		List<Document> docs = crawler.crawlDocuments();

		for (Document doc : docs) {
			annMecab.setProperty("target", "text");
			annMecab.annotate(doc); // throws Exception
//			System.err.println(doc.getKeywords().size());
		}

		long time2 = System.currentTimeMillis();

		System.err.println("time in ms: " + (time2 - time1));

		annMecab.close();

	}

}

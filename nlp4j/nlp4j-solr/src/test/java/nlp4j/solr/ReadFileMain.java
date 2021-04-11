package nlp4j.solr;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import nlp4j.Document;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;
import nlp4j.solr.importer.SolrDocumentImporter;

public class ReadFileMain {

	public static void main(String[] args) throws Exception {
		// "C:/usr/local/doshisha/iip_lab/共起概念ベース構築（完成版）/共起概念ベース構築（完成版）/wikipedia_for_kyouki_serasera.txt"

		String fileName = "C:/usr/local/doshisha/iip_lab/共起概念ベース構築（完成版）/共起概念ベース構築（完成版）/"
				+ "wikipedia_for_kyouki_serasera.txt";

//		ArrayList<Document> docs = new ArrayList<>();

		SolrDocumentImporter importer = new SolrDocumentImporter();

		// 1076657

		int skip = 1000 * 100;
		int max = skip + 1000 * 10000;

		try (LineIterator it = FileUtils.lineIterator(new File(fileName), "UTF-8");) {
			int count = 0;
			while (it.hasNext()) {
				count++;

				if (count < skip) {
					continue;
				}

				String line = it.nextLine();

//				System.err.println(line);

				String[] ss = line.split("\t");

				Document doc = new DefaultDocument();
				doc.putAttribute("id", "" + (count + 1));

				for (String s : ss) {
					Keyword kwd = new DefaultKeyword();
					kwd.setLex(s);
					kwd.setFacet("word");
					doc.addKeyword(kwd);
				}

//				docs.add(doc);

				importer.importDocument(doc);

				// do something with line

				if (count % 1000 == 0) {
					System.err.println(((double) count / (double) max) + " " + count);
				}

				if (count > max) {
					break;
				}
			}
		}

//		importer.importDocuments(docs);

		importer.commit();

		importer.close();

	}

}

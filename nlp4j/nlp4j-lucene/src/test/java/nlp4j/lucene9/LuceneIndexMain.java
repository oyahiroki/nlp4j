package nlp4j.lucene9;

import java.util.List;

import org.apache.lucene.document.Document;

public class LuceneIndexMain {

	/**
	 * sample main
	 */
	public static void main(String[] args) throws Exception {
	
		try (LuceneIndex index = new LuceneIndex()) {
	
			// --------------------
			// add document
			// --------------------
	
			Document doc1 = new Document();
			doc1.add(new org.apache.lucene.document.TextField("content", "Hello Lucene",
					org.apache.lucene.document.Field.Store.YES));
	
			index.add(doc1);
	
			Document doc2 = new Document();
			doc2.add(new org.apache.lucene.document.TextField("content", "Apache Lucene Search Engine",
					org.apache.lucene.document.Field.Store.YES));
	
			index.add(doc2);
	
			// --------------------
			// search
			// --------------------
	
			List<Document> docs = index.search("*:*", 10);
	
			System.out.println("HITS=" + docs.size());
	
			for (Document d : docs) {
				System.out.println(d.get("content"));
			}
	
			// --------------------
			// add again
			// --------------------
	
			Document doc3 = new Document();
			doc3.add(new org.apache.lucene.document.TextField("content", "Lucene supports near real time search",
					org.apache.lucene.document.Field.Store.YES));
	
			index.add(doc3);
	
			// --------------------
			// search again
			// --------------------
	
			List<Document> docs2 = index.search("content:Lucene", 10);
	
			System.out.println("HITS=" + docs2.size());
	
			for (Document d : docs2) {
				System.out.println(d.get("content"));
			}
		}
	}

}

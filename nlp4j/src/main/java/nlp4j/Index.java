package nlp4j;

import java.util.List;

public interface Index {

	void addDocument(Document doc);

	void addDocuments(List<Document> docs);

	List<Keyword> getKeywords(String facet);

	List<Keyword> getKeywords(String facet, String condition);

}
package nlp4j.lucene9;

import org.apache.lucene.document.Document;

/**
 * Represents a single search hit (matching document) from a search operation.
 * Contains the document ID, relevance score, and the document itself.
 */
public class SearchHit {

	private final String id;
	private final float score;
	private final Document document;

	/**
	 * Constructs a new SearchHit.
	 *
	 * @param id the document ID
	 * @param score the relevance score
	 * @param document the Lucene Document
	 */
	public SearchHit(String id, float score, Document document) {
		this.id = id;
		this.score = score;
		this.document = document;
	}

	/**
	 * Returns the document ID.
	 *
	 * @return the document ID
	 */
	public String id() {
		return id;
	}

	/**
	 * Returns the relevance score.
	 *
	 * @return the score
	 */
	public float score() {
		return score;
	}

	/**
	 * Returns the Lucene Document.
	 *
	 * @return the Document object
	 */
	public Document document() {
		return document;
	}
}
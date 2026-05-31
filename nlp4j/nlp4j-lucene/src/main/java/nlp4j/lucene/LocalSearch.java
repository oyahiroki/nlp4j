package nlp4j.lucene;

import java.io.IOException;

import org.apache.lucene.document.Document;

import nlp4j.json.JsonNode;
import nlp4j.lucene9.LuceneIndex;
import nlp4j.lucene9.LuceneLocalSearchApi;
import nlp4j.lucene9.SearchSchema;

/**
 * Simple local search engine wrapper for Lucene. Provides a simplified API for
 * adding documents and performing text searches with language-specific field
 * support (Japanese, English, or default).
 *
 * <p>
 * This class automatically manages the Lucene index lifecycle and provides a
 * high-level interface for common search operations.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * try (LocalSearch search = new LocalSearch("ja")) {
 * 	search.add("doc1", "東京の観光スポット");
 * 	search.add("doc2", "京都の寺院");
 * 	search.commit();
 *
 * 	SearchResult[] results = search.search("東京", 10);
 * 	for (SearchResult result : results) {
 * 		System.out.println(result.id + ": " + result.body);
 * 	}
 * }
 * </pre>
 */
public class LocalSearch implements AutoCloseable {

	private String language;
	private String default_field_name;

	SearchSchema schema;
	LuceneIndex index;
	LuceneLocalSearchApi api;

	/**
	 * Constructs a new LocalSearch instance with the specified language.
	 *
	 * @param language the language code ("ja" for Japanese, "en" for English, or
	 *                 any other value for default text field)
	 * @throws LocalSearchException if index initialization fails
	 */
	public LocalSearch(String language) {

		this.language = language;
		initIndex();
		this.schema = createSchema(0);
		this.default_field_name = resolveDefaultFieldName(language);
	}

	public LocalSearch(String language, int vectorDimension) {
		if (vectorDimension < 0) {
			throw new LocalSearchException("vectorDimension must be >= 0",
					new IllegalArgumentException("vectorDimension must be >= 0"));
		}

		this.language = language;
		initIndex();
		this.schema = createSchema(vectorDimension);
		this.default_field_name = resolveDefaultFieldName(language);
	}

	/**
	 * Adds a document to the search index.
	 *
	 * @param id   the unique identifier for the document
	 * @param body the text content to be indexed
	 * @throws LocalSearchException if adding the document fails
	 */
	public void add(String id, String body) {
		Document doc1 = schema.document() //
				.put("id", id) //
				.put(default_field_name, body) //
				.build();
		try {
			this.index.add(doc1);
		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	/**
	 * Adds a document from a JSON string. The JSON must contain "id" and "body"
	 * fields.
	 *
	 * <p>
	 * Example JSON format:
	 * </p>
	 * 
	 * <pre>
	 * {
	 *   "id": "doc1",
	 *   "body": "Document text content"
	 * }
	 * </pre>
	 *
	 * @param json_string the JSON string containing document data
	 * @throws LocalSearchException if JSON parsing or document addition fails
	 */
	public void addJson(String json_string) {
		try {
			JsonNode json = JsonNode.parse(json_string);
			String id = json.get("id").asString();
			String body = json.get("body").asString();
			add(id, body);
		} catch (Throwable th) {
			throw new LocalSearchException(th.getMessage(), th);
		}
	}

	/**
	 * Closes the search index and releases all resources. This method is
	 * automatically called when using try-with-resources.
	 *
	 * @throws LocalSearchException if closing the index fails
	 */
	@Override
	public void close() {
		if (this.index != null) {
			try {
				this.index.close();
			} catch (IOException e) {
				throw new LocalSearchException(e.getMessage(), e);
			}
		}
	}

	/**
	 * Commits all pending changes to the index. This method should be called after
	 * adding documents to make them searchable.
	 *
	 * @throws LocalSearchException if commit fails
	 */
	public void commit() {
		try {
			this.index.commit();
		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	private SearchSchema createSchema(int vectorDimension) {
		this.schema = createSchema(vectorDimension);

		return schema;
	}

	private void initIndex() {
		try {
			index = new LuceneIndex();
			api = new LuceneLocalSearchApi(index);
		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	private String resolveDefaultFieldName(String language) {
		if ("ja".equals(language)) {
			return "text_ja";
		} else if ("en".equals(language)) {
			return "text_en";
		} else {
			return "text";
		}
	}

	/**
	 * Performs a text search on the indexed documents.
	 *
	 * @param query the search query string
	 * @param limit the maximum number of results to return
	 * @return an array of SearchResult objects, ordered by relevance score
	 * @throws LocalSearchException if search fails
	 */
	public SearchResult[] search(String query, int limit) {
		JsonNode matchRequest = JsonNode.object();
		{
			JsonNode matchQuery = JsonNode.object();
			matchQuery.put("match", JsonNode.object().put(this.default_field_name, query));
			matchRequest.put("query", matchQuery);
			matchRequest.put("size", limit);
		}
		JsonNode result3;
		try {
			result3 = api.search("myindex/_search", matchRequest);

//			System.out.println(result3.toJson());

			int size = result3.get("hits").get("total").get("value").getAsInt();

			if (size < 1) {
				return new SearchResult[0];
			} else {
				SearchResult[] results = new SearchResult[size];

				JsonNode hits = result3.get("hits").get("hits");

				for (int n = 0; n < size; n++) {
					float score = (float) hits.get(n).get("_score").asDouble(-1);
					String id = hits.get(n).get("_source").get("id").asString();
					String text = hits.get(n).get("_source").get(default_field_name).asString();
					results[n] = new SearchResult();
					results[n].id = id;
					results[n].body = text;
					results[n].score = score;
				}

//				System.err.println(size);
				return results;
			}

		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

	@Override
	public String toString() {
		return "LocalSearch [language=" + language + ", default_field_name=" + default_field_name + ", schema=" + schema
				+ ", index=" + index + "]";
	}
}

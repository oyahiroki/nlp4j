package nlp4j.lucene;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.util.BytesRef;

import nlp4j.json.JsonNode;
import nlp4j.lucene9.FieldTypeDef;
import nlp4j.lucene9.LuceneIndex;
import nlp4j.lucene9.LuceneLocalSearchApi;
import nlp4j.lucene9.SearchSchema;

public class LocalSearch implements AutoCloseable {

	private String language;
	private String default_field_name;

	SearchSchema schema;
	LuceneIndex index;
	LuceneLocalSearchApi api;

	public LocalSearch(String language) {
		this.language = language;
		try {
			index = new LuceneIndex();
			api = new LuceneLocalSearchApi(index);
		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}

		schema = new SearchSchema();
		{
			schema.add("id", FieldTypeDef.keyword().stored(true));
			schema.add("text", FieldTypeDef.text().stored(true));
			schema.add("text_en", FieldTypeDef.text().stored(true));
			schema.add("text_ja", FieldTypeDef.text().stored(true));
		}

		if ("ja".equals(this.language)) {
			default_field_name = "text_ja";
		} //
		else if ("en".equals(this.language)) {
			default_field_name = "text_en";
		} //
		else {
			default_field_name = "text";
		}

	}

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

	public void commit() {
		try {
			this.index.commit();
		} catch (IOException e) {
			throw new LocalSearchException(e.getMessage(), e);
		}
	}

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
					String id = hits.get(n).get("_id").asString();
					float score = (float) hits.get(n).get("_score").asDouble(-1);
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
	public void close() {
		if (this.index != null) {
			try {
				this.index.close();
			} catch (IOException e) {
				throw new LocalSearchException(e.getMessage(), e);
			}
		}
	}

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
}

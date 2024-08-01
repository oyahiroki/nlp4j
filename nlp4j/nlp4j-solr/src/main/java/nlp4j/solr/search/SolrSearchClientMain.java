package nlp4j.solr.search;

import java.io.IOException;

import org.apache.solr.client.solrj.impl.BaseHttpSolrClient.RemoteSolrException;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentBuilder;
import nlp4j.llm.embeddings.EmbeddingAnnotator;

public class SolrSearchClientMain {

	public static void main(String[] args) throws Exception {
		// ベクトル検索をする

		String endPoint = "http://localhost:8983/";
		String indexName = "sandbox";

		String text = "今日はいい天気かもしれない。";

		if (args != null && args.length > 0) {
			text = args[0];
			System.err.println("text: " + text);
		}

		Document doc = (new DocumentBuilder()).text(text).build();

		{ // Vector
			// String vector_name = "vector";
			// String endPoint = "http://localhost:8888/";
			DocumentAnnotator ann = new EmbeddingAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(doc);
//			System.err.println("size_of_vector: " + doc.getAttributeAsListNumbers("vector").size());
		}

		try (SolrSearchClient client = new SolrSearchClient.Builder(endPoint).build()) {
			JsonObject responseObject = client.searchByVector(indexName, doc);
			{
				responseObject.remove("responseHeader");
				responseObject.addProperty("responseheader", "removed");
			}

			System.err.println(new GsonBuilder().setPrettyPrinting().create().toJson(responseObject));
		} catch (IOException | RemoteSolrException e) {
			System.err.println("SKIP THIS TEST: " + e.getMessage());
		}

	}
}

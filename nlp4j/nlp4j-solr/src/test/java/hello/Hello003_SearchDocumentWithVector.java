package hello;

import java.io.IOException;

import org.apache.solr.client.solrj.impl.BaseHttpSolrClient.RemoteSolrException;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.impl.DefaultDocument;
import nlp4j.llm.embeddings.EmbeddingAnnotator;
import nlp4j.solr.search.SolrSearchClient;

public class Hello003_SearchDocumentWithVector {

	public static void main(String[] args) throws Exception {
		// ベクトル検索をする

		String endPoint = "http://localhost:8983/";
		String indexName = "sandbox";

		Document doc = new DefaultDocument();
		{
			doc.putAttribute("id", "doc001");
			doc.putAttribute("text", "今日はいい天気かもしれない。");
		}

		{ // Vector
			// String vector_name = "vector";
			// String endPoint = "http://localhost:8888/";
			DocumentAnnotator ann = new EmbeddingAnnotator();
			ann.setProperty("target", "text");
			ann.annotate(doc);
			System.err.println("size_of_vector: " + doc.getAttributeAsListNumbers("vector").size());
		}

		try (SolrSearchClient client = new SolrSearchClient.Builder(endPoint).build()) {
			JsonObject responseObject = client.searchByVector(indexName, doc);
			{
				{
					responseObject.remove("responseHeader");
					responseObject.addProperty("responseheader", "removed");
				}
				{
					JsonArray docs = responseObject.get("response").getAsJsonObject().get("docs").getAsJsonArray();
					for (int n = 0; n < docs.size(); n++) {
						docs.get(n).getAsJsonObject().remove("vector");
					}
				}
			}
			System.err.println(new GsonBuilder().setPrettyPrinting().create().toJson(responseObject));
		} catch (IOException | RemoteSolrException e) {
			System.err.println("SKIP THIS TEST: " + e.getMessage());
		}

	}

}

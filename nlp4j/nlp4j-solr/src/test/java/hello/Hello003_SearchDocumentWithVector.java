package hello;

import java.io.IOException;

import org.apache.solr.client.solrj.impl.BaseHttpSolrClient.RemoteSolrException;

import com.google.gson.GsonBuilder;
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
		{
			SolrSearchClient client = new SolrSearchClient.Builder(endPoint).build();
			try {
				JsonObject responseObject = client.searchByVector(indexName, doc);
				System.err.println(new GsonBuilder().setPrettyPrinting().create().toJson(responseObject));
			} catch (IOException e) {
				System.err.println("SKIP THIS TEST: " + e.getMessage());
			} catch (RemoteSolrException e) {
				System.err.println("SKIP THIS TEST: " + e.getMessage());
			}
		}

	}

}

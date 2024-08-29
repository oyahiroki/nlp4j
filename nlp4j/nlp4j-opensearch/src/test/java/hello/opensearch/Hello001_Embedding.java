package hello.opensearch;

import java.util.ArrayList;
import java.util.List;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;
import nlp4j.llm.embeddings.EmbeddingAnnotator;
import nlp4j.util.DocumentUtil;

public class Hello001_Embedding {

	public static void main(String[] args) throws Exception {

		List<Document> docs = new ArrayList<>();
		{
			Document doc = new DefaultDocument();
			{
				doc.putAttribute("id", "doc001");
				// dynamic field
				// *_txt_ja
				doc.putAttribute("text", "今日はいい天気です。");
				doc.putAttribute("field1_s", "AAA");
				doc.putAttribute("field2_i", 100);
				doc.addKeyword(new DefaultKeyword(0, 2, "word.nn", "今日", "今日"));
				doc.addKeyword(new DefaultKeyword(3, 5, "word.adj", "いい", "いい"));
				doc.addKeyword(new DefaultKeyword(4, 6, "word.nn", "天気", "天気"));
			}
			docs.add(doc);
		}

		{ // Vector
			DocumentAnnotator ann = new EmbeddingAnnotator();
			ann.setProperty("target", "text");
			ann.setProperty("vector_name", "vector1024");
			ann.setProperty("endPoint", "http://localhost:8888/");
			ann.annotate(docs); // vector size is 1024
		}

		System.err.println(DocumentUtil.toJsonPrettyString(docs.get(0)));

	}

}

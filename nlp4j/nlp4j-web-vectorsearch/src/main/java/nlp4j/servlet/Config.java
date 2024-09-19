package nlp4j.servlet;

public class Config {
	static public String SOLR_ENDPOINT = System.getenv("SOLR_ENDPOINT") == null ? "http://localhost:8983/solr/" : System.getenv("SOLR_ENDPOINT");
	static public String SOLR_COLLECTION = "sandbox";

}

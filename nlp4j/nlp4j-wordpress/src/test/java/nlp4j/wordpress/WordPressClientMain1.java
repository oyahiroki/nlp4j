package nlp4j.wordpress;

import nlp4j.Document;
import nlp4j.NlpServiceResponse;

public class WordPressClientMain1 {

	public static void main(String[] args) throws Exception {
		String endpoint = System.getProperty("NLP4J_WORDPRESS_APIENDPOINT");
		String user = System.getProperty("NLP4J_WORDPRESS_APIUSER");
		String pass = System.getProperty("NLP4J_WORDPRESS_APIKEY");

		WordPressClient client = new WordPressClient(endpoint, user, pass);

		Document doc = (new WordPressDocumentBuilder()).title("test").content("test").status("draft").categories(41)
				.build();

		NlpServiceResponse res = client.post(doc);

		System.err.println(res.ok());
		System.err.println(res.getResponseCode());

	}

}
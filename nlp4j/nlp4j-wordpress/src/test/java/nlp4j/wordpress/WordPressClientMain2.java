package nlp4j.wordpress;

public class WordPressClientMain2 {

	public static void main(String[] args) throws Exception {
		String endpoint = System.getProperty("NLP4J_WORDPRESS_APIENDPOINT");
		String user = System.getProperty("NLP4J_WORDPRESS_APIUSER");
		String pass = System.getProperty("NLP4J_WORDPRESS_APIKEY");

		WordPressClient client = new WordPressClient(endpoint, user, pass);

		client.getCategories();

	}

}

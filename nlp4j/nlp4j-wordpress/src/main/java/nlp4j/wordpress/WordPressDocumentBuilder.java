package nlp4j.wordpress;

import nlp4j.Document;
import nlp4j.DocumentBuilder;

public class WordPressDocumentBuilder {

	private String title;
	private String status;
	private String content;
	private Number[] categories;

	public WordPressDocumentBuilder title(String title) {
		this.title = title;
		return this;
	}

	public WordPressDocumentBuilder status(String status) {
		this.status = status;
		return this;
	}

	public WordPressDocumentBuilder content(String content) {
		this.content = content;
		return this;
	}

	public WordPressDocumentBuilder categories(Number... categories) {
		this.categories = categories;
		return this;
	}

	public Document build() {
		Document doc = (new DocumentBuilder()) //
				.put("title", this.title) //
				.put("status", this.status) //
				.put("content", this.content) //
				.put("categories", this.categories) //
				.build(); //
		return doc;
	}

}

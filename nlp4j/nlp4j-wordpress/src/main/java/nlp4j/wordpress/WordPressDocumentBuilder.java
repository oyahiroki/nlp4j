package nlp4j.wordpress;

import nlp4j.Document;
import nlp4j.DocumentBuilder;

/**
 * <pre>
 * WordPress の記事データを構築するためのビルダークラスです。
 * This class is a builder for constructing WordPress article data.
 * </pre>
 */
public class WordPressDocumentBuilder {

	private String title; // 記事のタイトル // Title of the article
	private String status; // 記事のステータス // Status of the article
	private String content; // 記事の内容 // Content of the article
	private Number[] categories; // 記事のカテゴリーIDの配列 // Array of category IDs for the article

	/**
	 * <pre>
	     * タイトルを設定します。
	     * Sets the title.
	 * </pre>
	 * 
	 * @param title 設定するタイトル // Title to set
	 * @return このビルダー自身を返します。 // Returns this builder itself.
	 */
	public WordPressDocumentBuilder title(String title) {
		this.title = title;
		return this;
	}

	/**
	 * <pre>
	     * ステータスを設定します。
	     * Sets the status.
	 * </pre>
	 * 
	 * @param status 設定するステータス // Status to set
	 * @return このビルダー自身を返します。 // Returns this builder itself.
	 */
	public WordPressDocumentBuilder status(String status) {
		this.status = status;
		return this;
	}

	/**
	 * <pre>
	     * コンテンツを設定します。
	     * Sets the content.
	 * </pre>
	 * 
	 * @param content 設定するコンテンツ // Content to set
	 * @return このビルダー自身を返します。 // Returns this builder itself.
	 */
	public WordPressDocumentBuilder content(String content) {
		this.content = content;
		return this;
	}

	/**
	 * <pre>
	     * カテゴリーを設定します。
	     * Sets the categories.
	 * </pre>
	 * 
	 * @param categories 設定するカテゴリーIDの配列 // Array of category IDs to set
	 * @return このビルダー自身を返します。 // Returns this builder itself.
	 */
	public WordPressDocumentBuilder categories(Number... categories) {
		this.categories = categories;
		return this;
	}

	/**
	 * <pre>
	     * 構築されたDocumentオブジェクトを返します。
	     * Returns the built Document object.
	 * </pre>
	 * 
	 * @return 構築されたDocument // The built Document
	 */
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
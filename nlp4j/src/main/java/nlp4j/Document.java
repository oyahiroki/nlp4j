package nlp4j;

import java.util.List;

/**
 * ドキュメントクラスです。<br>
 * Document Class.
 * 
 * @author Hiroki Oya
 * @version 1.0
 * @since 1.0
 *
 */
public interface Document {

	/**
	 * キーワードを追加します。<br>
	 * Add Keyword.
	 * 
	 * @param keyword キーワード
	 * @since 1.0
	 */
	void addKeyword(Keyword keyword);

	/**
	 * 複数キーワードを追加します。<br>
	 * Add multiple Keywords.
	 * 
	 * @param kwds keyword キーワード
	 * @since 1.0
	 */
	void addKeywords(List<Keyword> kwds);

	/**
	 * 属性を返します。<br>
	 * Return Attribute value.
	 * 
	 * @param key 属性のキー
	 * @return 属性の値
	 * @since 1.0
	 */
	Object getAttribute(String key);

	/**
	 * 文書IDを返します。この値を用いてドキュメントを区別します。 <br>
	 * Return Document ID.
	 * 
	 * @return 文書ID
	 * @since 1.0
	 */
	String getId();

	/**
	 * この文書のキーワードを返します。<br>
	 * Return Keywords.
	 * 
	 * @return キーワード
	 * @since 1.0
	 */
	List<Keyword> getKeywords();

	/**
	 * この文書のテキストを返します。<br>
	 * Return Text.
	 * 
	 * @return テキスト
	 * @since 1.0
	 */
	String getText();

	/**
	 * この文書の属性をセットします。<br>
	 * Set Attribute of this document.
	 * 
	 * @param key   属性名
	 * @param value 属性の値
	 * @since 1.0
	 */
	void putAttribute(String key, String value);

	/**
	 * この文書のIDをセットします。<br>
	 * Set ID of this document.
	 * 
	 * @param id 文書のID
	 * @since 1.0
	 */
	void setId(String id);

	/**
	 * この文書のキーワードをセットします。<br>
	 * Set Keywords of this document.
	 * 
	 * @param keywords キーワード
	 * @since 1.0
	 */
	void setKeywords(List<Keyword> keywords);

	/**
	 * この文書のテキストをセットします。<br>
	 * Set text of this document.
	 * 
	 * @param text テキスト
	 * @since 1.0
	 */
	void setText(String text);

	/**
	 * この文書のキーワードをファセットで指定して返します。<br>
	 * Return keywords for a facet.
	 * 
	 * @param facet ファセット
	 * @return キーワード
	 * @since 1.0
	 */
	List<Keyword> getKeywords(String facet);

}

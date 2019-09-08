package nlp4j;

import java.util.List;

/**
 * ドキュメントクラスです。
 * 
 * @author Hiroki Oya
 * @version 1.0
 * @since 1.0
 *
 */
public interface Document {

	/**
	 * キーワードを追加します。
	 * 
	 * @param keyword キーワード
	 * @since 1.0
	 */
	void addKeyword(Keyword keyword);

	/**
	 * 複数キーワードを追加します。
	 * 
	 * @param keyword キーワード
	 * @since 1.0
	 */
	void addKeywords(List<Keyword> kwds);

	/**
	 * 属性を返します。
	 * 
	 * @param key 属性のキー
	 * @return 属性の値
	 * @since 1.0
	 */
	Object getAttribute(String key);

	/**
	 * 文書IDを返します。この値を用いてドキュメントを区別します。
	 * 
	 * @return 文書ID
	 * @since 1.0
	 */
	String getId();

	/**
	 * この文書のキーワードを返します。
	 * 
	 * @return キーワード
	 * @since 1.0
	 */
	List<Keyword> getKeywords();

	/**
	 * この文書のテキストを返します。
	 * 
	 * @return テキスト
	 * @since 1.0
	 */
	String getText();

	/**
	 * この文書の属性をセットします。
	 * 
	 * @param key
	 * @param value
	 * @since 1.0
	 */
	void putAttribute(String key, String value);

	/**
	 * この文書のIDをセットします。
	 * 
	 * @param id
	 * @since 1.0
	 */
	void setId(String id);

	/**
	 * この文書のキーワードをセットします。
	 * 
	 * @param keywords
	 * @since 1.0
	 */
	void setKeywords(List<Keyword> keywords);

	/**
	 * この文書のテキストをセットします。
	 * 
	 * @param text
	 * @since 1.0
	 */
	void setText(String text);

	/**
	 * この文書のキーワードをファセットで指定して返します。
	 * 
	 * @param facet
	 * @return
	 * @since 1.0
	 */
	List<Keyword> getKeywords(String facet);

}

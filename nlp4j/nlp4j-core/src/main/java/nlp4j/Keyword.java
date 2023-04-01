package nlp4j;

import java.io.Serializable;

/**
 * キーワード<br>
 * Keywords are usually words and phrases that are extracted from textual
 * content.
 * 
 * @author Hiroki Oya
 * @version 1.0
 */
public interface Keyword extends Serializable, Cloneable {

	static public final int BEGIN_INIT = -1;
	static public final double CORRELATION_INIT = -1;
	static public final int COUNT_INIT = -1;
	static public final int END_INIT = -1;
	static public final int SEQUENCE_INIT = -1;

	/**
	 * Shift begin, end
	 * 
	 * @param n
	 * @since 1.3.1.0
	 */
	void addBeginEnd(int n);

	/**
	 * キーワードの属性値を返します。<br>
	 * Return value for an attribute
	 * 
	 * @param attribute 属性名
	 * @return value 値
	 * @since 1.2
	 */
	String get(String attribute);

	/**
	 * キーワードの開始位置を返します。<br>
	 * Return begin of keyword.
	 * 
	 * @return begin 開始位置
	 * @since 1.0
	 */
	int getBegin();

	/**
	 * キーワードの共起性を返します。<br>
	 * Return keyword correlation.
	 * 
	 * @return correlation 共起性
	 * @since 1.0
	 */
	double getCorrelation();

	/**
	 * キーワードのカウントを返します。<br>
	 * Return keyword count.
	 * 
	 * @return count カウント
	 * @since 1.0
	 */
	long getCount();

	/**
	 * キーワードの終了位置を返します。<br>
	 * Return end of keyword.
	 * 
	 * @return end 終了位置
	 * @since 1.0
	 */
	int getEnd();

	/**
	 * ファセットを返します。<br>
	 * Return facet.
	 * 
	 * @return facet ファセット
	 * @since 1.0
	 */
	String getFacet();

	/**
	 * @return フラグ
	 * @since 1.3
	 */
	boolean getFlag();

	/**
	 * キーワードの正規形を返します。正規形は見出し語とも呼ばれます。<br>
	 * Return normalized string.
	 * 
	 * @return the lex 正規形
	 * @since 1.0
	 */
	String getLex();

	/**
	 * キーワードの名前空間を取得します。<br>
	 * Get namespace of keyword.
	 * 
	 * @since 1.3.1
	 */
	String getNamespance();

	/**
	 * キーワードの「読み」を返します。<br>
	 * Return reading of keyword.
	 * 
	 * @return reading 読み
	 * @since 1.0
	 */
	String getReading();

	/**
	 * キーワードの表出文字を返します。<br>
	 * Return string of keyword.
	 * 
	 * @return 表出文字
	 * @since 1.0
	 */
	String getStr();

	/**
	 * Universal POSを返します。<br>
	 * Return Universal POS.
	 * 
	 * @return Universal POS
	 * @since 1.3.1.0
	 */
	String getUPos();

	/**
	 * Return match result of Keyword
	 * 
	 * @param rule
	 * @return match result
	 * @since 1.3.1.0
	 */
	boolean match(Keyword rule);

	/**
	 * キーワードの開始位置をセットします。<br>
	 * Set begin of keyword.
	 * 
	 * @param begin 開始位置
	 * @since 1.0
	 */
	void setBegin(int begin);

	/**
	 * キーワードの共起性をセットします。<br>
	 * Set correlation of keyword.
	 * 
	 * @param d correlation
	 * @since 1.0
	 */
	void setCorrelation(double d);

	/**
	 * キーワードのカウントをセットします。<br>
	 * Set count of keyword.
	 * 
	 * @param count カウント
	 * @since 1.0
	 */
	void setCount(long count);

	/**
	 * キーワードの終了位置をセットします。<br>
	 * Set end of keyword.
	 * 
	 * @param end 終了位置
	 * @since 1.0
	 */
	void setEnd(int end);

	/**
	 * キーワードのファセットをセットします。<br>
	 * Set facet of keyword.
	 * 
	 * @param facet ファセット
	 * @since 1.0
	 */
	void setFacet(String facet);

	/**
	 * @param b フラグ
	 * @since 1.3
	 */
	void setFlag(boolean b);

	/**
	 * キーワードの正規形をセットします。正規形は見出し語とも呼ばれます。<br>
	 * Set normalized string of keyword.
	 * 
	 * @param lex 正規形
	 * @since 1.0
	 */
	void setLex(String lex);

	/**
	 * キーワードの名前空間をセットします。<br>
	 * Set namespace of keyword.
	 * 
	 * @param namespace
	 * @since 1.3.1
	 */
	void setNamespace(String namespace);

	/**
	 * キーワードの読みをセットします。<br>
	 * Set reading of keyword.
	 * 
	 * @param reading 読み
	 * @since 1.0
	 */
	void setReading(String reading);

	/**
	 * キーワードの表出文字をセットします。<br>
	 * Set string of keyword.
	 * 
	 * @param str 表出文字
	 * @since 1.0
	 */
	void setStr(String str);

	/**
	 * Universal POSをセットします。<br>
	 * Set Universal POS.
	 * 
	 * @param upos
	 */
	void setUPos(String upos);
}

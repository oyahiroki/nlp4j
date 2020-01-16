/**
 * 
 */
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
public interface Keyword extends Serializable {

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
	 * キーワードの正規形を返します。正規形は見出し語とも呼ばれます。<br>
	 * Return normalized string.
	 * 
	 * @return the lex 正規形
	 * @since 1.0
	 */
	String getLex();

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
	 * キーワードの正規形をセットします。正規形は見出し語とも呼ばれます。<br>
	 * Set normalized string of keyword.
	 * 
	 * @param lex 正規形
	 * @since 1.0
	 */
	void setLex(String lex);

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
}

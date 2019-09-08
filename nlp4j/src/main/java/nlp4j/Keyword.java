/**
 * 
 */
package nlp4j;

import java.io.Serializable;

/**
 * キーワード / Keywords are usually words and phrases that are extracted from
 * textual content.
 * 
 * @author Hiroki Oya
 * @version 1.0
 */
public interface Keyword extends Serializable {
	/**
	 * キーワードの開始位置を返します。
	 * 
	 * @return begin 開始位置
	 * @since 1.0
	 */
	int getBegin();

	/**
	 * キーワードの共起性を返します。
	 * 
	 * @return correlation 共起性
	 * @since 1.0
	 */
	double getCorrelation();

	/**
	 * キーワードのカウントを返します。
	 * 
	 * @return count カウント
	 * @since 1.0
	 */
	long getCount();

	/**
	 * キーワードの終了位置を返します。
	 * 
	 * @return end 終了位置
	 * @since 1.0
	 */
	int getEnd();

	/**
	 * ファセットを返します。
	 * 
	 * @return facet ファセット
	 * @since 1.0
	 */
	String getFacet();

	/**
	 * キーワードの正規形を返します。正規形は見出し語とも呼ばれます。
	 * 
	 * @return the lex 正規形
	 * @since 1.0
	 */
	String getLex();

	/**
	 * キーワードの「読み」を返します。
	 * 
	 * @return reading 読み
	 * @since 1.0
	 */
	String getReading();

	/**
	 * キーワードの表出文字を返します。
	 * 
	 * @return 表出文字
	 * @since 1.0
	 */
	String getStr();

	/**
	 * キーワードの開始位置をセットします。
	 * 
	 * @param begin 開始位置
	 * @since 1.0
	 */
	void setBegin(int begin);

	/**
	 * キーワードの共起性をセットします。
	 * 
	 * @param d correlation
	 * @since 1.0
	 */
	void setCorrelation(double d);

	/**
	 * キーワードのカウントをセットします。
	 * 
	 * @param count カウント
	 * @since 1.0
	 */
	void setCount(long count);

	/**
	 * キーワードの終了位置をセットします。
	 * 
	 * @param end 終了位置
	 * @since 1.0
	 */
	void setEnd(int end);

	/**
	 * キーワードのファセットをセットします。
	 * 
	 * @param facet ファセット
	 * @since 1.0
	 */
	void setFacet(String facet);

	/**
	 * キーワードの正規形をセットします。正規形は見出し語とも呼ばれます。
	 * 
	 * @param lex 正規形
	 * @since 1.0
	 */
	void setLex(String lex);

	/**
	 * キーワードの読みをセットします。
	 * 
	 * @param reading 読み
	 * @since 1.0
	 */
	void setReading(String reading);

	/**
	 * キーワードの表出文字をセットします。
	 * 
	 * @param str 表出文字
	 * @since 1.0
	 */
	void setStr(String str);
}

/**
 * 
 */
package nlp4j;

import java.io.Serializable;

/**
 * Keywords are usually words and phrases that are extracted from textual
 * content.
 * 
 * @author Hiroki Oya
 * @version 1.0
 */
public interface Keyword extends Serializable {
	/**
	 * @return begin 開始位置
	 */
	int getBegin();

	/**
	 * @return correlation 共起性
	 */
	double getCorrelation();

	/**
	 * @return count カウント
	 */
	long getCount();

	/**
	 * @return end 終了位置
	 */
	int getEnd();

	/**
	 * @return facet ファセット
	 */
	String getFacet();

	/**
	 * @return the lex 正規形
	 */
	String getLex();

	/**
	 * @return reading 読み
	 */
	String getReading();

	/**
	 * @return str 表出文字
	 */
	String getStr();

	/**
	 * @param begin 開始位置
	 */
	void setBegin(int begin);

	/**
	 * @param d correlation
	 */
	void setCorrelation(double d);

	/**
	 * @param count カウント
	 */
	void setCount(long count);

	/**
	 * @param end 終了位置
	 */
	void setEnd(int end);

	/**
	 * @param facet ファセット
	 */
	void setFacet(String facet);

	/**
	 * @param lex 正規形
	 */
	void setLex(String lex);

	/**
	 * @param reading 読み
	 */
	void setReading(String reading);

	/**
	 * @param str 表出文字
	 */
	void setStr(String str);
}

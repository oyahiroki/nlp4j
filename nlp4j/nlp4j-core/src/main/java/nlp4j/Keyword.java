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
	public void addBeginEnd(int n);

	/**
	 * キーワードの属性値を返します。<br>
	 * Return value for an attribute
	 * 
	 * @param attribute 属性名
	 * @return value 値
	 * @since 1.2
	 */
	public String get(String attribute);

	/**
	 * キーワードの開始位置を返します。<br>
	 * Return begin of keyword.
	 * 
	 * @return begin 開始位置
	 * @since 1.0
	 */
	public int getBegin();

	/**
	 * キーワードの共起性を返します。<br>
	 * Return keyword correlation.
	 * 
	 * @return correlation 共起性
	 * @since 1.0
	 */
	public double getCorrelation();

	/**
	 * キーワードのカウントを返します。<br>
	 * Return keyword count.
	 * 
	 * @return count カウント
	 * @since 1.0
	 */
	public long getCount();

	/**
	 * キーワードのカウントを返します。<br>
	 * Return keyword count.
	 * 
	 * @return count カウント
	 * @since 1.3.7.9
	 */
	public long getCount2();

	/**
	 * キーワードの終了位置を返します。<br>
	 * Return end of keyword.
	 * 
	 * @return end 終了位置
	 * @since 1.0
	 */
	public int getEnd();

	/**
	 * ファセットを返します。<br>
	 * Return facet.
	 * 
	 * @return facet ファセット
	 * @since 1.0
	 */
	public String getFacet();

	/**
	 * @return フラグ
	 * @since 1.3
	 */
	public boolean getFlag();

	/**
	 * @return ID of this keyword
	 * @since 1.3.7.9
	 */
	public String getId();

	/**
	 * キーワードの正規形を返します。正規形は見出し語とも呼ばれます。<br>
	 * Return normalized string.
	 * 
	 * @return the lex 正規形
	 * @since 1.0
	 */
	public String getLex();

	/**
	 * キーワードの名前空間を取得します。<br>
	 * Get namespace of keyword.
	 * 
	 * @since 1.3.1
	 */
	public String getNamespance();

	/**
	 * パラグラフのインデックスを返します<br>
	 * Return index of sentence
	 * 
	 * @return index of Paragraph
	 * @since 1.3.7.8
	 */
	public int getParagraphIndex();

	/**
	 * キーワードの「読み」を返します。<br>
	 * Return reading of keyword.
	 * 
	 * @return reading 読み
	 * @since 1.0
	 */
	public String getReading();

	/**
	 * 文のインデックスを返します<br>
	 * Return index of sentence
	 * 
	 * @return index of sentence
	 * @since 1.3.7.8
	 */
	public int getSentenceIndex();

	/**
	 * @return 連番
	 * @since 1.3.7.8
	 */
	public int getSequence();

	/**
	 * キーワードの表出文字を返します。<br>
	 * Return string of keyword.
	 * 
	 * @return 表出文字
	 * @since 1.0
	 */
	public String getStr();

	/**
	 * Universal POSを返します。<br>
	 * Return Universal POS.
	 * 
	 * @return Universal POS
	 * @since 1.3.1.0
	 */
	public String getUPos();

	/**
	 * Return match result of Keyword
	 * 
	 * @param rule
	 * @return match result
	 * @since 1.3.1.0
	 */
	public boolean match(Keyword rule);

	/**
	 * キーワードの開始位置をセットします。<br>
	 * Set begin of keyword.
	 * 
	 * @param begin 開始位置
	 * @since 1.0
	 */
	public void setBegin(int begin);

	/**
	 * キーワードの共起性をセットします。<br>
	 * Set correlation of keyword.
	 * 
	 * @param d correlation
	 * @since 1.0
	 */
	public void setCorrelation(double d);

	/**
	 * キーワードのカウントをセットします。<br>
	 * Set count of keyword.
	 * 
	 * @param count カウント
	 * @since 1.0
	 */
	public void setCount(long count);

	/**
	 * キーワードのカウントをセットします。<br>
	 * Set count of keyword.
	 * 
	 * @param count カウント
	 * @since 1.3.7.2
	 */
	public void setCount2(long count);

	/**
	 * キーワードの終了位置をセットします。<br>
	 * Set end of keyword.
	 * 
	 * @param end 終了位置
	 * @since 1.0
	 */
	public void setEnd(int end);

	/**
	 * キーワードのファセットをセットします。<br>
	 * Set facet of keyword.
	 * 
	 * @param facet ファセット
	 * @since 1.0
	 */
	public void setFacet(String facet);

	/**
	 * @param b フラグ
	 * @since 1.3
	 */
	public void setFlag(boolean b);

	/**
	 * @param id of this keyword
	 * @since 1.3.7.9
	 */
	public void setId(String id);

	/**
	 * キーワードの正規形をセットします。正規形は見出し語とも呼ばれます。<br>
	 * Set normalized string of keyword.
	 * 
	 * @param lex 正規形
	 * @since 1.0
	 */
	public void setLex(String lex);

	/**
	 * キーワードの名前空間をセットします。<br>
	 * Set namespace of keyword.
	 * 
	 * @param namespace
	 * @since 1.3.1
	 */
	public void setNamespace(String namespace);

	/**
	 * パラグラフのインデックスをセットします<br>
	 * Set index of Paragraph
	 * 
	 * @param paragraphIndex
	 * @since 1.3.7.8
	 */
	public void setParagraphIndex(int paragraphIndex);

	/**
	 * キーワードの読みをセットします。<br>
	 * Set reading of keyword.
	 * 
	 * @param reading 読み
	 * @since 1.0
	 */
	public void setReading(String reading);

	/**
	 * 文のインデックスをセットします<br>
	 * Set index of sentence
	 * 
	 * @param sentenceIndex
	 * @since 1.3.7.8
	 */
	public void setSentenceIndex(int sentenceIndex);

	/**
	 * @param sequence 連番
	 * @since 1.3.7.8
	 */
	public void setSequence(int sequence);

	/**
	 * キーワードの表出文字をセットします。<br>
	 * Set string of keyword.
	 * 
	 * @param str 表出文字
	 * @since 1.0
	 */
	public void setStr(String str);

	/**
	 * Universal POSをセットします。<br>
	 * Set Universal POS.
	 * 
	 * @param upos
	 */
	public void setUPos(String upos);
}

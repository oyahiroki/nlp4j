package nlp4j.impl;

import nlp4j.Keyword;

/**
 * ドキュメントのキーワードとなるクラスです。<br>
 * Keyword class of a document.
 * 
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class DefaultKeyword implements Keyword, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int begin = -1;
	double correlation;
	long count = -1;
	int end = -1;
	String facet;
	private boolean flag = false;
	String lex;
	String namespace;

	String reading;

	int sequence = -1;

	String str;

	/**
	 * Default constructor
	 * 
	 * @since 1.2.1.0
	 */
	public DefaultKeyword() {
		super();
	}

	/**
	 * @param begin 開始位置
	 * @param end   終了位置
	 * @param facet ファセット
	 * @param lex   正規形
	 * @param str   表出文字
	 */
	public DefaultKeyword(int begin, int end, String facet, String lex, String str) {
		super();
		this.begin = begin;
		this.end = end;
		this.facet = facet;
		this.lex = lex;
		this.str = str;
	}

	/**
	 * @param namespace 名前空間
	 * @param begin     開始位置
	 * @param end       終了位置
	 * @param facet     ファセット
	 * @param lex       正規形
	 * @param str       表出文字
	 * @since 1.3.1
	 */
	public DefaultKeyword(String namespace, int begin, int end, String facet, String lex, String str) {
		super();
		this.begin = begin;
		this.end = end;
		this.facet = facet;
		this.lex = lex;
		this.str = str;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Keyword) {
			Keyword kw = (Keyword) obj;
			String facet = kw.getFacet();
			String lex = kw.getLex();

			if (this.facet == null && facet == null) {
				return this.lex.equals(lex);
			} else if (this.lex == null || lex == null || this.facet == null || facet == null) {
				return false;
			} else {
				return this.facet.equals(facet) && this.lex.equals(lex);
			}
		} else {
			return super.equals(obj);
		}

	}

	@Override
	public String get(String attribute) {

		if (attribute == null) {
			return null;
		} //
		else if ("facet".equals(attribute)) {
			return facet;
		} //
		else if ("lex".equals(attribute)) {
			return lex;
		} //
		else if ("str".equals(attribute)) {
			return str;
		} //
		else if ("reading".equals(attribute)) {
			return reading;
		} //
		else if ("count".equals(attribute)) {
			return "" + count;
		} //
		else if ("begin".equals(attribute)) {
			return "" + begin;
		} //
		else if ("end".equals(attribute)) {
			return "" + end;
		} //
		else if ("correlation".equals(attribute)) {
			return "" + correlation;
		} //
		else if ("sequence".equals(attribute)) {
			return "" + sequence;
		} //
		else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nlp4j.impl.Keywd#getBegin()
	 */
	@Override
	public int getBegin() {
		return begin;
	}

	@Override
	public double getCorrelation() {
		return this.correlation;
	}

	@Override
	public long getCount() {
		return this.count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nlp4j.impl.Keywd#getEnd()
	 */
	@Override
	public int getEnd() {
		return end;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nlp4j.impl.Keywd#getFacet()
	 */
	@Override
	public String getFacet() {
		return facet;
	}

	/**
	 * フラグを返します。
	 * 
	 * @return フラグ
	 * @since 1.3
	 */
	@Override
	public boolean getFlag() {
		return this.flag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nlp4j.impl.Keywd#getLex()
	 */
	@Override
	public String getLex() {
		return lex;
	}

	@Override
	public String getNamespance() {
		return this.namespace;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nlp4j.Keyword#getReading()
	 */
	@Override
	public String getReading() {

		return this.reading;
	}

	/**
	 * @return 連番
	 */
	public int getSequence() {
		return sequence;
	}

	/**
	 * @return the str
	 */
	public String getStr() {
		return str;
	}

	@Override
	public int hashCode() {
		if (this.lex == null || lex == null || this.facet == null || facet == null) {
			return super.hashCode();
		} else {
			return (facet + lex).hashCode();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nlp4j.impl.Keywd#setBegin(int)
	 */
	@Override
	public void setBegin(int begin) {
		this.begin = begin;
	}

	@Override
	public void setCorrelation(double d) {
		this.correlation = d;
	}

	@Override
	public void setCount(long count) {
		this.count = count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nlp4j.impl.Keywd#setEnd(int)
	 */
	@Override
	public void setEnd(int end) {
		this.end = end;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nlp4j.impl.Keywd#setFacet(java.lang.String)
	 */
	@Override
	public void setFacet(String facet) {
		this.facet = facet;
	}

	/**
	 * フラグをセットします。
	 * 
	 * @param b フラグ
	 * @since 1.3
	 */
	@Override
	public void setFlag(boolean b) {
		this.flag = b;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nlp4j.impl.Keywd#setLex(java.lang.String)
	 */
	@Override
	public void setLex(String lex) {
		this.lex = lex;
	}

	@Override
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nlp4j.Keyword#setReading(java.lang.String)
	 */
	@Override
	public void setReading(String reading) {
		this.reading = reading;
	}

	/**
	 * 連番を返します。
	 * 
	 * @param sequence 連番
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;

	}

	/**
	 * @param str the str to set
	 */
	public void setStr(String str) {
		this.str = str;
	}

	@Override
	public String toString() {
		return //
		this.lex //
				+ " [" //
//				+ "sequence=" + sequence //
				+ "facet=" + facet //
//				+ ", lex=" + lex //
				+ ", str=" + str //
//				+ ", reading="+ reading  //
//				+ ", count=" + count  //
//				+ ", begin=" + begin  //
//				+ ", end=" + end  //
//				+ ", correlation=" + correlation //
				+ "]";
	}

	/**
	 * @return キーワードの詳細情報文字列
	 * @since 1.3
	 */
	public String toStringDetail() {
		return this.lex + " [sequence=" + sequence + ", facet=" + facet + ", lex=" + lex + ", str=" + str + ", reading="
				+ reading + ", count=" + count + ", begin=" + begin + ", end=" + end + ", correlation=" + correlation
				+ "]";
	}

}

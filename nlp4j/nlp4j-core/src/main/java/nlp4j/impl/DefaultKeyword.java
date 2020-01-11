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
	String facet;
	String lex;
	String str;
	String reading;
	long count = -1;
	int begin = -1;
	int end = -1;
	double correlation;

	int sequence = -1;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see nlp4j.impl.Keywd#getLex()
	 */
	@Override
	public String getLex() {
		return lex;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see nlp4j.impl.Keywd#setLex(java.lang.String)
	 */
	@Override
	public void setLex(String lex) {
		this.lex = lex;
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
	public int hashCode() {
		if (this.lex == null || lex == null || this.facet == null || facet == null) {
			return super.hashCode();
		} else {
			return (facet + lex).hashCode();
		}
	}

	@Override
	public String toString() {
		return this.lex + " [sequence=" + sequence + ", facet=" + facet + ", lex=" + lex + ", str=" + str + ", reading="
				+ reading + ", count=" + count + ", begin=" + begin + ", end=" + end + ", correlation=" + correlation
				+ "]";
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}

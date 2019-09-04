package nlp4j.impl;

import nlp4j.Keyword;

/**
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class DefaultKeyword implements Keyword {
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

	int sequence=-1;

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
		return "DefaultKeyword [sequence=" + sequence + ", facet=" + facet + ", lex=" + lex + ", str=" + str
				+ ", reading=" + reading + ", count=" + count + ", begin=" + begin + ", end=" + end + ", correlation="
				+ correlation + "]";
	}


}


/**
 * 
 */
package nlp4j.impl;

import nlp4j.Keyword;

/**
 * @author oyahiroki
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

	int begin = -1;
	int end = -1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Keyword o) {
		// TODO Auto-generated method stub
		return 0;
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
	 * @param str the str to set
	 */
	public void setStr(String str) {
		this.str = str;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Keyword [facet=" + facet + ", lex=" + lex + ", str=" + str + ", reading=" + reading + ", begin=" + begin
				+ ", end=" + end + "]";
	}

}




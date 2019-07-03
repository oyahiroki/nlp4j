/**
 * 
 */
package nlp4j;

import java.io.Serializable;

/**
 * Keywords are usually words and phrases that are extracted from textual
 * content.
 */
public interface Keyword extends Comparable<Keyword>, Serializable {
	/**
	 * @return the begin
	 */
	int getBegin();

	/**
	 * @return the end
	 */
	int getEnd();

	/**
	 * @return the facet
	 */
	String getFacet();

	/**
	 * @return the lex
	 */
	String getLex();

	/**
	 * @return the reading
	 */
	String getReading();

	/**
	 * @return the str
	 */
	String getStr();

	/**
	 * @param begin the begin to set
	 */
	void setBegin(int begin);

	/**
	 * @param end the end to set
	 */
	void setEnd(int end);

	/**
	 * @param facet the facet to set
	 */
	void setFacet(String facet);

	/**
	 * @param lex the lex to set
	 */
	void setLex(String lex);

	/**
	 * @param lex the reading to set
	 */
	void setReading(String reading);

	/**
	 * @param lex the str to set
	 */
	void setStr(String str);

}

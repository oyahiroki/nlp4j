package nlp4j.util;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultKeywordWithDependency;

/**
 * @author Hiroki Oya
 * @created_at 2021-05-07
 * @since 1.3.1.0
 */
public class KeywordHandler extends DefaultHandler {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	boolean closed = false;

	int depth_of_w = 0;

	private List<KeywordWithDependency> keywords = new ArrayList<KeywordWithDependency>();

	private KeywordWithDependency ptr;

	String qName = null;

	/**
	 * @return Parsed Keywords
	 */
	public List<KeywordWithDependency> getKeywords() {
		return this.keywords;
	}

	public void startDocument() throws SAXException {
		logger.info("Document Start");
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		logger.debug("qName=" + qName);

		this.qName = qName;

		// <pattern>

		// <w>
		if (qName.equals("w")) {
			depth_of_w++;
			logger.debug("depthW=" + depth_of_w);

			// IF ROOT
			if (depth_of_w == 1) {
				KeywordWithDependency kwdRoot = new DefaultKeywordWithDependency();
				this.ptr = kwdRoot;
				this.keywords.add(kwdRoot);
			} //
				// ELSE( NOT ROOT)
			else {
				KeywordWithDependency keywordRule = new DefaultKeywordWithDependency();
				this.ptr.addChild(keywordRule);
				this.ptr = keywordRule;
			}

			// FOR EACH ATTRIBUTES
			for (int i = 0; i < attributes.getLength(); i++) {
				// fixed: getLocalName -> getQName
				String key = attributes.getQName(i);
				String value = attributes.getValue(i);

				logger.info("" + key + "=" + value);

				if (key.equals("begin")) {
					ptr.setBegin(Integer.parseInt(value));
				} //
				else if (key.equals("end")) {
					ptr.setEnd(Integer.parseInt(value));
				} //
				else if (key.equals("facet")) {
					ptr.setFacet(value);
				} //
				else if (key.equals("id")) {
					// do nothing
				} //
				else if (key.equals("lex")) {
					ptr.setLex(value);
				} //
				else if (key.equals("sequence")) {
					ptr.setSequence(Integer.parseInt(value));
				} //
				else if (key.equals("str")) {
					ptr.setStr(value);
				} //
				else if (key.equals("upos")) {
					ptr.setUPos(value);
				} //
				else if (key.equals("reading")) {
					ptr.setReading(value);
				} //
				else if (key.equals("relation")) {
					ptr.setRelation(value);
				} //
				else {
					// do nothing
				}

			} // END OF FOR EACH Attributes

		} // </w>

	}

	public void endElement(String uri, String localName, String qName) throws SAXException {

		logger.debug("end element: " + qName);

		this.qName = null;

		if (this.ptr != null) {
			this.ptr = this.ptr.getParent();
		}

		if (qName.equals("w") && depth_of_w == 0) {
			this.ptr = null;
		}
	}

}

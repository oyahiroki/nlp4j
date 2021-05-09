package nlp4j.pattern;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parse Patterns from Pattern XML
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 * &lt;patterns lang="ja"&gt;
 * 	&lt;!-- example: いい ... 天気 --&gt;
 * 	&lt;pattern facet="pattern.sample" value="{0.lex} ... {1.lex}"&gt;
 * 	 &lt;w id="1" upos="NOUN" lex="天気"&gt;
 * 	  &lt;w id="0" upos="ADJ" /&gt;
 * 	 &lt;/w&gt;
 * 	&lt;/pattern&gt;
 * 	&lt;pattern facet="pattern.sample" value="{0.lex} ... {1.lex}" disabled="true"&gt;
 * 	 &lt;w id="1" upos="NOUN"&gt;
 * 	  &lt;w id="0" upos="ADJ" /&gt;
 * 	 &lt;/w&gt;
 * 	&lt;/pattern&gt;
 * &lt;/patterns&gt;
 * </pre>
 * 
 * @author Hiroki Oya
 * @created_at 2021-05-03
 * @since 1.3.1.0
 */
public class UserPatternHandler extends DefaultHandler {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	boolean closed = false;

	int depth_of_w = 0;

	private String lang = null;

	Pattern pattern;

	ArrayList<Pattern> patterns = new ArrayList<>();

	private KeywordRule ptr;

	String qName = null;

	boolean skipPattern = false;

	public List<Pattern> getPatterns() {
		return patterns;
	}

	public void startDocument() throws SAXException {
		logger.info("Document Start");
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		logger.debug("qName=" + qName);

		this.qName = qName;

		// <patterns>
		if (qName.equals("patterns")) {
			this.lang = attributes.getValue("lang");
		} // </pattterns>

		// <pattern>
		else if (qName.equals("pattern")) {

			if (attributes.getValue("disabled") != null //
					&& attributes.getValue("disabled").equals("true")) {
				this.skipPattern = true;
				logger.info("disabled pattern");
				return;
			}

			Pattern pattern = new Pattern();
			{
				pattern.setFacet(attributes.getValue("facet"));
				pattern.setValue(attributes.getValue("value"));
				pattern.setLang(this.lang);
			}
			this.pattern = pattern;

			patterns.add(pattern);

		} // </pattern>

		// <w>
		// 20200123 skipPattern==false
		else if (qName.equals("w") && skipPattern == false) {
			depth_of_w++;
			logger.debug("depthW=" + depth_of_w);

			// IF ROOT
			if (depth_of_w == 1) {
				KeywordRule keywordRule = new KeywordRule();
				this.pattern.setKeywordRule(keywordRule);
				this.ptr = keywordRule;
			} //
				// ELSE( NOT ROOT)
			else {
				KeywordRule keywordRule = new KeywordRule();
				this.ptr.addChild(keywordRule);
//				keywordRule.setParent(this.ptr);
				this.ptr = keywordRule;
			}

			// FOR EACH ATTRIBUTES
			for (int i = 0; i < attributes.getLength(); i++) {
				// fixed: getLocalName -> getQName
				String key = attributes.getQName(i);
				String value = attributes.getValue(i);

				logger.info("key=" + key + ",value=" + value);

				if (key.equals("id")) {
					ptr.setId(value);
				} //
				else if (key.equals("upos")) {
					ptr.setUPos(value);
				} //
				else if (key.equals("lex")) {
//					ptr.setLexPattern(value);
					ptr.setLex(value);
				} //

			} // END OF FOR EACH Attributes

		} // </w>

	}

	public void endElement(String uri, String localName, String qName) throws SAXException {

		logger.debug("end element: " + qName);

		this.qName = null;

		if (this.ptr != null) {
			this.ptr = (KeywordRule) this.ptr.getParent();
		}

		if ("patterns".equals(qName)) {

		} //
		else if ("pattern".equals(qName)) {
			skipPattern = false;
		} //
		else if ("w".equals(qName) && skipPattern == false) {
			depth_of_w--;
		}

		if (qName.equals("w") && depth_of_w == 0) {
			this.pattern = null;
			this.ptr = null;
		}
	}

}

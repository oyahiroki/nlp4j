/**
 * 
 */
package nlp4j.yhoo_jp;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import nlp4j.Keyword;
import nlp4j.impl.DefaultKeyword;
import nlp4j.xml.AbstractXmlHandler;

/**
 * 日本語形態素解析
 * 
 * @author oyahiroki
 *
 */
public class MaServiceResponseHandler extends AbstractXmlHandler {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	ArrayList<Keyword> keywords = new ArrayList<>();
	DefaultKeyword kwd;

	public ArrayList<Keyword> getKeywords() {
		return keywords;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nlp4j.yhoo_jp.AbstractXmlHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		logger.debug(super.getPath());
		logger.debug(super.getText());

		if ("ResultSet/ma_result/word_list/word/surface".equals(super.getPath())) {
			kwd = new DefaultKeyword();
			kwd.setStr(super.getText());
		} //
		else if ("ResultSet/ma_result/word_list/word/reading".equals(super.getPath())) {
			kwd.setReading(super.getText());
		} //
		else if ("ResultSet/ma_result/word_list/word/pos".equals(super.getPath())) {
			kwd.setFacet(super.getText());
		} //
		else if ("ResultSet/ma_result/word_list/word/baseform".equals(super.getPath())) {
			kwd.setLex(super.getText());
		} //
		else if ("ResultSet/ma_result/word_list/word".equals(super.getPath())) {
			keywords.add(kwd);
			kwd = new DefaultKeyword();
		} //
		super.endElement(uri, localName, qName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nlp4j.yhoo_jp.AbstractXmlHandler#endDocument()
	 */
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

}


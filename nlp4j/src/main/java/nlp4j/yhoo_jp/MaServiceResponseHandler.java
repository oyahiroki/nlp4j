/**
 * 
 */
package nlp4j.yhoo_jp;

import java.util.ArrayList;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import nlp4j.Keyword;
import nlp4j.impl.KeywordImpl;

/**
 * 日本語形態素解析
 * 
 * @author oyahiroki
 *
 */
public class MaServiceResponseHandler extends AbstractXmlHandler {

	ArrayList<KeywordImpl> keywords = new ArrayList<>();
	KeywordImpl kwd;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nlp4j.yhoo_jp.AbstractXmlHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		System.err.println(super.getPath());
		System.err.println(super.getText());

		if ("ResultSet/ma_result/word_list/word/surface".equals(super.getPath())) {
			kwd = new KeywordImpl();
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
			kwd = new KeywordImpl();
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

		for (Keyword kwd : keywords) {
			System.err.println("--> " + kwd.toString());
		}

	}

}

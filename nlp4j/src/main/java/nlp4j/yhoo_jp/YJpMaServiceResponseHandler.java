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
 * Yahoo! Japan Developer Network が提供する形態素解析処理のレスポンスXMLをパースするクラスです。<br>
 * Yahoo! Japan Japanese language Morphological analysis Response XML Handler
 * 
 * @author Hiroki Oya
 * @version 1.0
 * @since 1.0
 *
 */
public class YJpMaServiceResponseHandler extends AbstractXmlHandler {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	ArrayList<Keyword> keywords = new ArrayList<>();
	DefaultKeyword kwd;
	String sentence;

	/**
	 * @param sentence 自然言語文字列
	 */
	public YJpMaServiceResponseHandler(String sentence) {
		super();
		this.sentence = sentence;
	}

	/**
	 * @return キーワード
	 */
	public ArrayList<Keyword> getKeywords() {
		return keywords;
	}

	int maxBegin = 0;

	int sequence = 0;

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
			sequence++;
			kwd = new DefaultKeyword();
			kwd.setStr(super.getText());
			kwd.setSequence(sequence);
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

			{
				int begin = sentence.indexOf(kwd.getStr(), maxBegin);
				kwd.setBegin(begin);
				kwd.setEnd(begin + kwd.getStr().length());
				maxBegin = begin;
			}

//			sequence++;
//			kwd = new DefaultKeyword();
//			kwd.setSequence(sequence);
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

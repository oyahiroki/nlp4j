package nlp4j.xml;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * XMLハンドラーの抽象クラスです。<br>
 * Abstact XML Handler.
 * 
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public abstract class AbstractXmlHandler extends DefaultHandler {

	protected Stack<String> qNameStack = new Stack<>();
	protected StringBuffer sb = new StringBuffer();

	protected String getPath() {
		return String.join("/", qNameStack.toArray(new String[0]));
	}

	protected String getText() {
		if (sb != null) {
			return sb.toString();
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
	 */
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		qNameStack.push(qName);
		sb = new StringBuffer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		qNameStack.pop();
		sb = new StringBuffer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		sb.append(new String(ch, start, length));
	}

}

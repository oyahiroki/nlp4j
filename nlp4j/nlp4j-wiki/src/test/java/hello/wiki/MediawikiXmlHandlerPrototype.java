package hello.wiki;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import nlp4j.xml.AbstractXmlHandler;

public class MediawikiXmlHandlerPrototype extends AbstractXmlHandler {

	String pageTitle = null;
	String pageId = null;
	String pageFormat = null;
	String pageText = null;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (qName.equals("page")) {

			// 255425

			this.pageTitle = null;
			this.pageId = null;
			this.pageFormat = null;
			this.pageText = null;
		}
//		System.err.println(qName);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (super.getPath().equals("mediawiki/page/title")) {
			this.pageTitle = super.getText();
		} //
		else if (super.getPath().equals("mediawiki/page/id")) {
			this.pageId = super.getText();
		} //
		else if (super.getPath().equals("mediawiki/page/revision/format")) {
			this.pageFormat = super.getText();
		} //
		else if (super.getPath().equals("mediawiki/page/revision/text")) {
			this.pageText = super.getText();
		} //
		else if (super.getPath().equals("mediawiki/page")) {
//			if (this.pageId != null && this.pageId.equals("32")) 
			{
				System.err.println(this.pageTitle);
				System.err.println(this.pageId);
				System.err.println(this.pageFormat);
				System.err.println(this.pageText);
			}
		} //

//		System.err.println(super.getPath());

		// end of process
		super.endElement(uri, localName, qName);
	}

}

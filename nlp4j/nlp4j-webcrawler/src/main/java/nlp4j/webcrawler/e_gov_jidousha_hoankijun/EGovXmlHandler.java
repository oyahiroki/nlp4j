package nlp4j.webcrawler.e_gov_jidousha_hoankijun;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import nlp4j.xml.AbstractXmlHandler;

public class EGovXmlHandler extends AbstractXmlHandler {

	Map<String, String> textMap = new LinkedHashMap<>();

	List<String> pathList = new ArrayList<>();

	private PrintWriter pw;

	public EGovXmlHandler(PrintWriter pw) {
		this.pw = pw;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
//		System.err.println(uri);
//		System.err.println(localName);
//		System.err.println(qName);
//		System.err.println(super.getPath());
//		System.err.println(super.getText());
		textMap.put(super.getPath(), super.getText());

		if (this.pathList.contains(super.getPath()) == false) {
			this.pathList.add(super.getPath());
		}

		//
		if (getPath().equals("Law/LawBody/MainProvision/Chapter/Article/Paragraph/ParagraphSentence/Sentence")) {
			System.err.println(super.getText());
		}

		super.endElement(uri, localName, qName);
	}

	@Override
	public void endDocument() throws SAXException {

		super.endDocument();

		{
			System.err.println("<path>");

			for (String path : this.pathList) {
				System.err.println(path);
			}

			System.err.println("</path>");
		}

	}

}

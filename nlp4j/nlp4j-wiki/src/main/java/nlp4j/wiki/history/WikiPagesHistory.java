package nlp4j.wiki.history;

import java.io.File;

import org.xml.sax.Attributes;

import nlp4j.xml.SAXParserUtils;
import nlp4j.xml.StandardXmlHandler;

public class WikiPagesHistory {

	public static void main(String[] args) throws Exception {

//		String fileName = "D:\\local\\wiki\\jawiki\\20250901\\jawiki\\20250901\\jawiki-20250901-pages-meta-history2.xml-p390360p390428\\jawiki-20250901-pages-meta-history2.xml-p390360p390428";
//		File f = new File(fileName);

		File f = new File(
				"D:\\local\\wiki\\jawiki\\20250901\\jawiki\\20250901\\jawiki-20250901-pages-meta-history2.xml-p390360p390428.7z");

		StandardXmlHandler h = new StandardXmlHandler() {

			@Override
			public void startElement(String uri, String localName, String qName, Attributes attributes, String path) {
//				System.err.println("start path: " + path);
			}

			@Override
			public void endElement(String uri, String localName, String qName, String path, String text) {
				if (path.contains("contributor")) {
					System.err.println("end path: " + path);
					System.err.println("text:" + text.trim());
				} //
				else if (path.contains("timestamp")) {
					System.err.println("end path: " + path);
					System.err.println("text:" + text.trim());
				}

			}

		};
		SAXParserUtils.parse(f, h);
	}

}

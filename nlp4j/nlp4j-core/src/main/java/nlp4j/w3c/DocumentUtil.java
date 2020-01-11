package nlp4j.w3c;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class DocumentUtil {

	static public Document parse(File xmlFile) throws IOException {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setIgnoringComments(true);
			dbFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder builder = dbFactory.newDocumentBuilder();
			Document document1 = builder.parse(xmlFile);
			return document1;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	static public Document parse(String xmlString) throws IOException {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setIgnoringComments(true);
			dbFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder builder = dbFactory.newDocumentBuilder();
			Document document1 = builder.parse(new ByteArrayInputStream(xmlString.getBytes("utf-8")));
			return document1;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}

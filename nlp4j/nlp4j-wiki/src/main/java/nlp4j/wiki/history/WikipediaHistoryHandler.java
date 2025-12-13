package nlp4j.wiki.history;

import java.io.IOException;

import org.xml.sax.Attributes;

import nlp4j.util.CsvWriter;
import nlp4j.xml.StandardXmlHandler;

public class WikipediaHistoryHandler extends StandardXmlHandler {

	String title = null;
	String timestamp = null;
	String username = null;
	String userid = null;
	
	CsvWriter writer;
	
	public void setWriter(CsvWriter writer) {
		this.writer = writer;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes,
			String path) {
//	System.err.println("start path: " + path);
	}

	@Override
	public void endElement(String uri, String localName, String qName, String path, String text) {
		if ("mediawiki/page/title".equals(path)) {
			title = (text.trim());
		} //
		else if ("mediawiki/page/revision/contributor/username".equals(path)) {
			username = (text.trim());
		} //
		else if ("mediawiki/page/revision/contributor/id".equals(path)) {
			userid = (text.trim());
		} //
		else if ("mediawiki/page/revision/contributor/ip".equals(path)) {
			username = (text.trim());
			userid = username;
		} //
		else if ("mediawiki/page/revision/timestamp".equals(path)) {
			timestamp = (text.trim());
		} //
		else if ("mediawiki/page/revision".equals(path)) {
//			System.out.println("title=" + title + ",timestamp=" + timestamp + ",username=" + username
//					+ ",userid=" + userid);
			
			try {
				writer.write(timestamp, title, username, userid);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} //
		else if ("mediawiki/page".equals(path)) {
			title = null;
		}
	}

}

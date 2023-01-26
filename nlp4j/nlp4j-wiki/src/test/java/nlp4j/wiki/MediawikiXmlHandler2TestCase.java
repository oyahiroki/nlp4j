package nlp4j.wiki;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;

public class MediawikiXmlHandler2TestCase extends TestCase {

	public void testGetPages101() throws Exception {

		String fileName = "src/test/resources/nlp4j.wiki/jawiktionary-dump-fragment2.xml";

		// XML Handler for Media Wiki
		MediawikiXmlHandler2 handler = new MediawikiXmlHandler2();

		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();

		InputStream bais = new FileInputStream(new File(fileName));

		saxParser.parse(bais, handler);

		HashMap<String, WikiPage> pages = handler.getPages();

		System.err.println(pages.keySet());

		for (WikiPage p : pages.values()) {
			System.err.println(p.toString());
		}

		// Expected Output
//		WikiPage [title=教育, id=11340, timestamp=2021-02-13T02:11:42Z, format=text/x-wiki, text={{Wikipedia|教育}}..., xml=null]
//		WikiPage [title=学校, id=11349, timestamp=2021-02-20T01:19:41Z, format=text/x-wiki, text={{Wikipedia|学校}}..., xml=null]
//		WikiPage [title=くう, id=11342, timestamp=2020-10-11T13:52:12Z, format=text/x-wiki, text={{同音の漢字|クウ}}=={..., xml=null]
//		WikiPage [title=純虚数, id=11341, timestamp=2018-02-21T14:27:27Z, format=text/x-wiki, text==={{ja}}== [[Ca..., xml=null]
//		WikiPage [title=こう, id=11344, timestamp=2019-09-10T14:25:29Z, format=text/x-wiki, text={{同音漢字}}=={{ja}..., xml=null]
//		WikiPage [title=けう, id=11343, timestamp=2018-04-07T04:33:32Z, format=text/x-wiki, text==={{ja}}==[[Cat..., xml=null]
//		...
//		WikiPage [title=徳島県, id=11364, timestamp=2018-07-05T18:30:40Z, format=text/x-wiki, text===日本語=={{Wikipe..., xml=null]
//		WikiPage [title=香川県, id=11363, timestamp=2019-12-02T12:39:04Z, format=text/x-wiki, text===日本語=={{Wikipe..., xml=null]
//		WikiPage [title=富山県, id=11366, timestamp=2018-07-05T15:30:35Z, format=text/x-wiki, text===日本語=={{Wikipe..., xml=null]
//		WikiPage [title=高知県, id=11365, timestamp=2010-06-13T03:51:14Z, format=text/x-wiki, text===日本語=={{Wikipe..., xml=null]
//		WikiPage [title=福井県, id=11368, timestamp=2020-12-21T12:10:44Z, format=text/x-wiki, text===日本語=={{Wikipe..., xml=null]
//		WikiPage [title=石川県, id=11367, timestamp=2019-12-02T12:39:14Z, format=text/x-wiki, text===日本語=={{Wikipe..., xml=null]
//		WikiPage [title=在る, id=11373, timestamp=2020-07-11T04:17:11Z, format=text/x-wiki, text==={{ja}}==[[cat..., xml=null]
//		WikiPage [title=有る, id=11372, timestamp=2020-07-11T04:18:11Z, format=text/x-wiki, text==={{ja}}==[[cat..., xml=null]
//		WikiPage [title=さる, id=11375, timestamp=2020-07-24T12:27:00Z, format=text/x-wiki, text==={{jpn}}==[[Ca..., xml=null]
//		WikiPage [title=みる, id=11374, timestamp=2020-06-15T16:07:11Z, format=text/x-wiki, text==={{jpn}}==[[Ca..., xml=null]
//		WikiPage [title=見る, id=11377, timestamp=2020-07-11T21:47:01Z, format=text/x-wiki, text==={{jpn}}==[[Ca..., xml=null]
//		WikiPage [title=去る, id=11376, timestamp=2020-07-11T11:47:23Z, format=text/x-wiki, text==={{ja}}==[[cat..., xml=null]
//		WikiPage [title=得る, id=11379, timestamp=2020-07-11T06:20:21Z, format=text/x-wiki, text={{DEFAULTSORT:える..., xml=null]
//		WikiPage [title=然る, id=11378, timestamp=2020-07-11T11:54:53Z, format=text/x-wiki, text==={{ja}}==[[Cat..., xml=null]

	}

	public void testGetPages102() throws Exception {

		String fileName = "src/test/resources/nlp4j.wiki/jawiktionary-dump-fragment3.xml";

		// XML Handler for Media Wiki
		MediawikiXmlHandler2 handler = new MediawikiXmlHandler2();

		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();

		InputStream bais = new FileInputStream(new File(fileName));

		saxParser.parse(bais, handler);

		HashMap<String, WikiPage> pages = handler.getPages();

		System.err.println(pages.keySet());

		WikiPage page = pages.get("11349");

		System.err.println(page);

	}

	public void testGetPages201() throws Exception {

		// Plain (Not compressed) XML file
		String fileName = "src/test/resources/nlp4j.wiki/" //
				+ "jawiki-20221101-pages-articles-multistream-255425.xml";

		// XML Handler for Media Wiki
		MediawikiXmlHandler2 handler = new MediawikiXmlHandler2();

		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();

		InputStream bais = new FileInputStream(new File(fileName));

		saxParser.parse(bais, handler);

		HashMap<String, WikiPage> pages = handler.getPages();

//		System.err.println(pages.keySet());

		// First Page
		WikiPage p = pages.values().toArray(new WikiPage[0])[0];

		System.err.println(p.getPlainText());

//		for (WikiPage p : pages.values()) {
//			System.err.println(p.getPlainText());
//		}
	}

	public void testGetPages202() throws Exception {

		// Plain (Not compressed) XML file
		String fileName = "src/test/resources/nlp4j.wiki/" //
				+ "jawiki-20221101-pages-articles-multistream-255425.xml";

		// XML Handler for Media Wiki
		MediawikiXmlHandler2 handler = new MediawikiXmlHandler2();

		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();

		InputStream bais = new FileInputStream(new File(fileName));

		saxParser.parse(bais, handler);

		HashMap<String, WikiPage> pages = handler.getPages();

//		System.err.println(pages.keySet());

		// First Page
		WikiPage p = pages.values().toArray(new WikiPage[0])[0];

		System.err.println(p.getText());

//		for (WikiPage p : pages.values()) {
//			System.err.println(p.getPlainText());
//		}
	}

}

package nlp4j.webcrawler.e_gov_jidousha_hoankijun;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import nlp4j.util.DateUtils;
import nlp4j.util.IOUtils;

public class XmlToJsonConverter {

	public static void main(String[] args) throws Exception {

		String fileName = "file/nlp4j.webcrawler.e_gov_jidousha_hoankijun/326M50000800067_20230701_504M60000800091_xml/"
				+ "326M50000800067_20230701_504M60000800091.xml";

		String outFileName = "file/nlp4j.webcrawler.e_gov_jidousha_hoankijun/326M50000800067_20230701_504M60000800091_"
				+ DateUtils.get_yyyyMMdd_HHmmss() + ".json";

		PrintWriter pw = IOUtils.printWriter(outFileName);

		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();

		EGovXmlHandler handler = new EGovXmlHandler(pw);

		InputStream bais = new FileInputStream(fileName);

		saxParser.parse(bais, handler);
	}

}

package nlp4j.pattern;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * created_at: 2021-05-03
 * 
 * @author Hiroki Oya
 * @since 1.3.1.0
 */
public class PatternReader {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * @param xml : File of Pattern XML
	 * @return null on Error
	 */
	static public List<Pattern> readFromXml(File xml) {
		try {
			List<Pattern> patterns = readFromXml(new FileInputStream(xml));
			if (patterns != null) {
				logger.info("Patterns loaded: " + patterns.size());
			} //
			else {
				logger.warn("Patterns not loaded. " + xml.getAbsolutePath());
			}
			return patterns;
		} catch (Exception e) {
			logger.warn("Exception: " + e.getMessage());
			e.printStackTrace();
			logger.error(e);
			return null;
		}
	}

	/**
	 * created_at: 2022-05-12
	 * 
	 * @param is : Input Stream of Pattern XML
	 * @return null on Error
	 * 
	 */
	static public List<Pattern> readFromXml(InputStream is) {
		try {

			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

			SAXParser saxParser = saxParserFactory.newSAXParser();

			UserPatternHandler handler = new UserPatternHandler();

			saxParser.parse(is, handler);

			List<Pattern> patterns = handler.getPatterns();
			if (patterns != null) {
				logger.info("Patterns loaded: " + patterns.size());
			} //
			else {
				logger.warn("Patterns not loaded. ");
			}
			return patterns;

		} catch (Exception e) {
			logger.warn("Exception: " + e.getMessage());
			e.printStackTrace();
			logger.error(e);
			return null;
		}

	}

}

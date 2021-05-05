package nlp4j.pattern;

import java.io.File;
import java.io.FileInputStream;
import java.lang.invoke.MethodHandles;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Hiroki Oya
 * @created_at 2021-05-03
 * @since 1.3.1.0
 */
public class PatternReader {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	static public List<Pattern> readFromXml(File xml) {
		try {

			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

			SAXParser saxParser = saxParserFactory.newSAXParser();

			UserPatternHandler handler = new UserPatternHandler();

			saxParser.parse(new FileInputStream(xml), handler);

			List<Pattern> patterns = handler.getPatterns();
			if (patterns != null) {
				logger.info("Patterns loaded: " + patterns.size());
			} //
			else {
				logger.warn("Patterns not loaded: " + xml.getAbsolutePath());
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

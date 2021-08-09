package nlp4j;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import junit.framework.TestCase;

public class LoggerTestCase extends TestCase {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public void test001() throws Exception {
		logger.info("info");
		logger.debug("debug");
		logger.error("error", new Exception("test"));
	}

}

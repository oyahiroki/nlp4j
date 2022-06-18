package log4j.test;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import junit.framework.TestCase;
import nlp4j.test.TestUtils;

public class TestUtilsTestCase extends TestCase {

	static private final Logger logger //
			= LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public void testSetLevelDebug() {

		logger.debug("hi 1"); // not printed

		TestUtils.setLevelDebug();

		logger.debug("hi 2"); // printed
	}

}

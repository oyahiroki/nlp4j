package log4j.test;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.test.NLP4JTestCase;

public class NLP4JTestCaseTestCase extends NLP4JTestCase {

	static private final Logger logger //
			= LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public NLP4JTestCaseTestCase() {
		super();
		target = NLP4JTestCase.class;
	}

	public void test001() throws Exception {
		System.err.println("OK");
		logger.info("info");
		logger.debug("debug");
	}

}

package nlp4j.logger;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.util.LoggerUtils;

public class HelloLoggerMain2 {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) throws Exception {
		LoggerUtils.setLoggerDebug();
		logger.info("Hi, this is info."); // printed
		logger.debug("Hi, this is debug."); // printed

	}

}

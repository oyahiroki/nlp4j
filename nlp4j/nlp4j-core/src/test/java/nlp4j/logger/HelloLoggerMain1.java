package nlp4j.logger;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HelloLoggerMain1 {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) throws Exception {
		logger.info("Hi, this is info."); // printed
		logger.debug("Hi, this is debug."); // not printed on default setting

	}

}

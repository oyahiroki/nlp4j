package hello.logger;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Hiroki Oya
 * @created_at 2021-08-20
 */
public class HelloLogger {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) {

		logger.info("Hello");

	}

}
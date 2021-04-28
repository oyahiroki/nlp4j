package nlp4j.test;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import junit.framework.TestCase;

/**
 * @author Hiroki Oya
 * @created_at 2021-04-24
 * @since 1.0.0.0
 */
public class NLP4JTestCase extends TestCase {

	static private final Logger logger //
			= LogManager.getLogger(MethodHandles.lookup().lookupClass());

	static {
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();
		LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
		loggerConfig.setLevel(Level.DEBUG);
		ctx.updateLoggers();
		logger.info("set level debug");
	}

	protected String description;

	@SuppressWarnings("rawtypes")
	protected Class target;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		logger.info("Testing name :" + super.getName());

		// skip check for This Class
		if (super.getName() != null && super.getName().equals("testAnalyticsTestCase001")) {
			return;
		}

		if (target == null) {
			throw new RuntimeException("target is not set.");
		}

		logger.info("Testing target:" + target.getName() + "...");

		if (super.getName().matches(".*[0-9]{3}") == false) {
			throw new RuntimeException("method name format is not : .*[0-9]{3}");
		}
	}

	@Override
	protected void tearDown() throws Exception {
		if (description == null) {
			logger.warn("WARNING: description is not set.");
		}
		description = null;
		super.tearDown();
	}

}

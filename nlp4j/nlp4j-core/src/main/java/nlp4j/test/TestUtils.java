package nlp4j.test;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

/**
 * @author Hiroki Oya
 *
 */
public class TestUtils {

	static public void setLevelDebug() {
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();
		LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
		loggerConfig.setLevel(Level.DEBUG);
		ctx.updateLoggers(); // This causes all Loggers to refetch information from their LoggerConfig.
	}

	/**
	 * @since 1.3.7.12
	 */
	static public void setLevelInfo() {
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();
		LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
		loggerConfig.setLevel(Level.INFO);
		ctx.updateLoggers(); // This causes all Loggers to refetch information from their LoggerConfig.
	}

}

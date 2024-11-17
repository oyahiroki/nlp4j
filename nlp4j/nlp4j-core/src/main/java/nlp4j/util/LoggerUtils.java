package nlp4j.util;

import org.apache.logging.log4j.core.config.Configurator;

/**
 * Utility class for managing Logger levels.
 * 
 * @since 1.3.7.15
 */
public class LoggerUtils {

	/**
	 * Sets the root Logger level to DEBUG.
	 * 
	 * This will affect all loggers that inherit from the root logger.
	 * 
	 * @since 1.3.7.15
	 */
	static public void setLoggerDebug() {
		Configurator.setRootLevel(org.apache.logging.log4j.Level.DEBUG);
	}

	/**
	 * Sets the specified Logger's level to DEBUG.
	 * 
	 * @param loggerName The name of the Logger whose level should be set to DEBUG
	 * @since 1.3.7.15
	 */
	static public void setLoggerDebug(String loggerName) {
		Configurator.setLevel(loggerName, org.apache.logging.log4j.Level.DEBUG);
	}

	/**
	 * Sets the root Logger level to INFO.
	 * 
	 * This will affect all loggers that inherit from the root logger.
	 * 
	 * @since 1.3.7.15
	 */
	static public void setLoggerInfo() {
		Configurator.setRootLevel(org.apache.logging.log4j.Level.INFO);
	}

	/**
	 * Sets the specified Logger's level to INFO.
	 * 
	 * @param loggerName The name of the Logger whose level should be set to INFO.
	 * @since 1.3.7.15
	 */
	static public void setLoggerInfo(String loggerName) {
		Configurator.setLevel(loggerName, org.apache.logging.log4j.Level.INFO);
	}
}

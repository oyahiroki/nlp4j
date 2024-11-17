package nlp4j.servlet.util;

import java.lang.invoke.MethodHandles;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppContextListener implements ServletContextListener {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("Webアプリケーションが起動しました。");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("Webアプリケーションがシャットダウンしました。");
	}

}

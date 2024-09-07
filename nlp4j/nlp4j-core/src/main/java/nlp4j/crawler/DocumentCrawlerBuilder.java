package nlp4j.crawler;

import java.lang.reflect.InvocationTargetException;

/**
 * created on : 2024-09-07
 */
public class DocumentCrawlerBuilder {

	Crawler crawler;

	public DocumentCrawlerBuilder(Class<? extends Crawler> classOfT) {

		try {
			this.crawler = classOfT.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	public DocumentCrawlerBuilder set(String propertyKey, String propertyValue) {
		this.crawler.setProperty(propertyKey, propertyValue);
		return this;
	}

	public DocumentCrawlerBuilder p(String propertyKey, String propertyValue) {
		this.crawler.setProperty(propertyKey, propertyValue);
		return this;
	}

	public Crawler build() {
		return this.crawler;
	}

}

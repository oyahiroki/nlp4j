package nlp4j.wiki.trend;

import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.util.DateUtils;

public class WikiTreandUtilsTestCase extends TestCase {

	public void testGetUrls001() {
		Date date = DateUtils.getDateFromNowByHours(2 + 2);
		List<String> urls = WikiTreandUtils.getUrls(date, 2);

		for (String url : urls) {
			System.err.println(url);
		}
	}

	public void testGetUrls002() {
		Date date = DateUtils.getDateFromNowByHours(24);
		List<String> urls = WikiTreandUtils.getUrls(date, 0);

		for (String url : urls) {
			System.err.println(url);
		}
	}

	public void testGetUrls003() {
		Date date = DateUtils.getDateFromNowByHours(24);
		List<String> urls = WikiTreandUtils.getUrls(date, 23);

		for (String url : urls) {
			System.err.println(url);
		}
	}

}

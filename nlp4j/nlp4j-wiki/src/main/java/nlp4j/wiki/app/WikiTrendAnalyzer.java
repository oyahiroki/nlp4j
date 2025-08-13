package nlp4j.wiki.app;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import nlp4j.counter.Counter;
import nlp4j.util.DateUtils;
import nlp4j.wiki.pageview.PageViewCounter;
import nlp4j.wiki.trend.WikiTreandUtils;

public class WikiTrendAnalyzer {

	public static void main(String[] args) throws Exception {

		Date date = DateUtils.getDateFromNowByHours(2 + 2);
		List<String> urls = WikiTreandUtils.getUrls(date, 2);

		PageViewCounter pageViewCounter = new PageViewCounter();
		Counter<String> counter_all = new Counter<String>();
		String[] ignore = { "メインページ", "特別:検索", "特別:最近の更新" };
		counter_all.addIgnore(ignore);

		for (String url : urls) {
			System.err.println(url);
			try {
				Counter<String> counter = new Counter<String>();
				counter.addIgnore(ignore);
				pageViewCounter.get(url, "ja", counter); // throws IOException
				counter.addIgnore(ignore);
				counter_all.add(counter);
				counter.top(10).forEach(cnt -> {
					System.err.println(cnt);
				});

			} catch (IOException e) {
				e.printStackTrace();
			}
			System.err.println("---");
		}
		counter_all.top(10).forEach(cnt -> {
			System.err.println(cnt);
		});

	}

}

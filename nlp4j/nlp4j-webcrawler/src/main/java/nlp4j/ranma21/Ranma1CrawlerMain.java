package nlp4j.ranma21;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import nlp4j.Document;
import nlp4j.crawler.Crawler;
import nlp4j.webcrawler.AbstractWebCrawler;

public class Ranma1CrawlerMain extends AbstractWebCrawler implements Crawler {

	@Override
	public List<Document> crawlDocuments() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) throws Exception {
		String[] urls = { //
				"https://shogakukan-comic.jp/book?isbn=9784091265012", //
				"https://shogakukan-comic.jp/book?isbn=9784091265029", //
				"https://shogakukan-comic.jp/book?isbn=9784091265036", //
				"https://shogakukan-comic.jp/book?isbn=9784091265043", //
				"https://shogakukan-comic.jp/book?isbn=9784091265050", //
				"https://shogakukan-comic.jp/book?isbn=9784091265067", //
				"https://shogakukan-comic.jp/book?isbn=9784091265074", //
				"https://shogakukan-comic.jp/book?isbn=9784091265081", //
				"https://shogakukan-comic.jp/book?isbn=9784091265098", //
				"https://shogakukan-comic.jp/book?isbn=9784091265104", //
				"https://shogakukan-comic.jp/book?isbn=9784091265111", //
				"https://shogakukan-comic.jp/book?isbn=9784091265128", //
				"https://shogakukan-comic.jp/book?isbn=9784091265135", //
				"https://shogakukan-comic.jp/book?isbn=9784091265142", //
				"https://shogakukan-comic.jp/book?isbn=9784091265159", //
				"https://shogakukan-comic.jp/book?isbn=9784091265166", //
				"https://shogakukan-comic.jp/book?isbn=9784091265173", //
				"https://shogakukan-comic.jp/book?isbn=9784091265180", //
				"https://shogakukan-comic.jp/book?isbn=9784091265197", //
				"https://shogakukan-comic.jp/book?isbn=9784091265203", //
				"https://shogakukan-comic.jp/book?isbn=9784091265210", //
				"https://shogakukan-comic.jp/book?isbn=9784091265227", //
				"https://shogakukan-comic.jp/book?isbn=9784091265234", //
				"https://shogakukan-comic.jp/book?isbn=9784091265241", //
				"https://shogakukan-comic.jp/book?isbn=9784091265258", //
				"https://shogakukan-comic.jp/book?isbn=9784091265265", //
				"https://shogakukan-comic.jp/book?isbn=9784091265272", //
				"https://shogakukan-comic.jp/book?isbn=9784091265289", //
				"https://shogakukan-comic.jp/book?isbn=9784091265296", //
				"https://shogakukan-comic.jp/book?isbn=9784091265302", //
				"https://shogakukan-comic.jp/book?isbn=9784091265319", //
				"https://shogakukan-comic.jp/book?isbn=9784091265326", //
				"https://shogakukan-comic.jp/book?isbn=9784091265333", //
				"https://shogakukan-comic.jp/book?isbn=9784091265340", //
				"https://shogakukan-comic.jp/book?isbn=9784091265357", //
				"https://shogakukan-comic.jp/book?isbn=9784091265364", //
				"https://shogakukan-comic.jp/book?isbn=9784091265371", //
				"https://shogakukan-comic.jp/book?isbn=9784091265388", //

		};

		int story = 1;

		for (int n = 0; n < urls.length; n++) {

			String url = urls[n];

			org.jsoup.nodes.Document document = Jsoup.parse(new URL(url), 1000 * 10); // throws IOException

			Elements els = document.select(".description > dd > div");

			if (els.size() == 0) {
				break;
			} else {
				Element el = els.get(0);
				System.err.println((n + 1));
				System.err.println(el.text());

				String text = el.text();

				FileUtils.write(new File("file/ranma/ranma_" + (n + 1) + ".txt"), text, "UTF-8", false);

			}

			Thread.sleep(1000);
		}

	}

}

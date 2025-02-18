package nlp4j.webcrawler.starbucksjapan;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class StarbucksStoreNumberCrawler000 {

	public static void main(String[] args) throws Exception {
		String url = "https://store.starbucks.co.jp/detail-32/";

		org.jsoup.nodes.Document document = Jsoup.parse(new URL(url), 1000 * 10); // throws IOException

		Elements els = document.select(".store-detail__title-text");

		{
			System.out.println(els.get(0).text());
		}

	}

}

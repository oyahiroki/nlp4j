package nlp4j.webcrawler.starbucksjapan;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class StarbucksStoreNumberCrawler001 {

	public static void main(String[] args) throws Exception {
		int storeNum = 32;
		String url = "https://store.starbucks.co.jp/detail-" + storeNum + "/";

		try {
			org.jsoup.nodes.Document document = Jsoup.parse(new URL(url), 1000 * 10); // throws IOException

			Elements els = document.select(".store-detail__title-text");

			{
				System.out.println(storeNum + "\t" + els.get(0).text() + "\t" + url);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

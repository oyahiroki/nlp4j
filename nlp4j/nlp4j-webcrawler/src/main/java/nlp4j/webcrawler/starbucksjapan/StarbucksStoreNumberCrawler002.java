package nlp4j.webcrawler.starbucksjapan;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import nlp4j.util.DateUtils;
import nlp4j.util.IOUtils;

public class StarbucksStoreNumberCrawler002 {

	public static void main(String[] args) throws Exception {
		File file = new File(
				"file/nlp4j.webcrawler.starbucksjapan" + "/out_" + DateUtils.get_yyyyMMdd_HHmmss() + ".txt");

		try (PrintWriter pw = IOUtils.printWriter(file);) {
			for (int n = 1 + 1000; n <= (1000 + 1000); n++) {
				getStoreNumber(n, pw);
				Thread.sleep(1000);
			}

		}

	}

	private static void getStoreNumber(int storeNum, PrintWriter pw) {
//		int storeNum = 32;
		String url = "https://store.starbucks.co.jp/detail-" + storeNum + "/";

		try {
			org.jsoup.nodes.Document document = Jsoup.parse(new URL(url), 1000 * 10); // throws IOException

			Elements els = document.select(".store-detail__title-text");

			{
				pw.println(storeNum + "\t" + els.get(0).text() + "\t" + url);
				System.out.println(storeNum + "\t" + els.get(0).text() + "\t" + url);
			}

		} catch (IOException e) {
//			e.printStackTrace();
//			pw.println(storeNum + "\t" + "NOT_FOUND" + "\t" + url);
			System.out.println(storeNum + "\t" + "NOT_FOUND" + "\t" + url);
		}

		pw.flush();

	}

}

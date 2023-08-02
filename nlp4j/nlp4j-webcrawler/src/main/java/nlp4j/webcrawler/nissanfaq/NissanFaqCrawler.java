package nlp4j.webcrawler.nissanfaq;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.crawler.Crawler;
import nlp4j.util.IOUtils;
import nlp4j.webcrawler.AbstractWebCrawler;

public class NissanFaqCrawler extends AbstractWebCrawler implements Crawler {

	@Override
	public List<Document> crawlDocuments() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) throws Exception {

//		String[] urls = { //
//				"https://faq2.nissan.co.jp/faq/show/" + "64936" + "?site_domain=default", //
//		};

		List<String> urls = new ArrayList<>();

//		urls.add("https://faq2.nissan.co.jp/faq/show/" + "64936" + "?site_domain=default");

		// 1-370 NOTFOUND

//		int start = 1000;
//		int end = 9999;

		int start = 10000;
		int end = 19999;
		
		for (int n = start; n <= end; n++) {
			urls.add("https://faq2.nissan.co.jp/faq/show/" + n + "?site_domain=default");

		}

		File outFile = File.createTempFile("nlp4j_webcrawler_nissanfaq_" + start + "_" + end, ".txt");

		PrintWriter pw = IOUtils.printWriter(outFile);

		for (int n = 0; n < urls.size(); n++) {

			String url = urls.get(n);

			org.jsoup.nodes.Document document = Jsoup.parse(new URL(url), 1000 * 10); // throws IOException

			int size = document.select(".okw_bread_list").size();

			JsonObject jo = new JsonObject();

			String id = url.substring("https://faq2.nissan.co.jp/faq/show/".length(), url.indexOf("?"));
			jo.addProperty("id", id);

			jo.addProperty("url", url);

			if (size == 0) {
				System.err.println("404," + url);
				jo.addProperty("response", 404);
				pw.println(jo.toString());
				Thread.sleep(500);
				continue;
			}

			System.err.println("200," + url);
			jo.addProperty("response", 200);

//			System.err.println(document.select(".okw_bread_list").get(0).text());
			jo.addProperty("bread_list", document.select(".okw_bread_list").get(0).text());

//			System.err.println(document.select(".faq_qstCont_ttl").get(0).text());
			jo.addProperty("q", document.select(".faq_qstCont_ttl").get(0).text());

//			System.err.println(document.select(".faq_ans_col").get(0).text());
			jo.addProperty("a_text", document.select(".faq_ans_col").get(0).text());

//			System.err.println(document.select(".faq_ans_col").get(0).html());
			jo.addProperty("a_html", document.select(".faq_ans_col").get(0).html());

			pw.println(jo.toString());

//			Elements els = document.select(".description > dd > div");
//
//			if (els.size() == 0) {
//				break;
//			} else {
//				Element el = els.get(0);
//				System.err.println((n + 1));
//				System.err.println(el.text());
//
//				String text = el.text();
//
//				FileUtils.write(new File("file/ranma/ranma_" + (n + 1) + ".txt"), text, "UTF-8", false);
//
//			}

			Thread.sleep(1000);
		}

		System.err.println(outFile.getAbsolutePath());

	}

}

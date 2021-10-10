package nlp4j1crawler;

import java.util.ArrayList;

import nlp4j.Document;
import nlp4j.twtr.crawler.TwitterCrawler;

public class TwitterCrawlerExample {
	public static void main(String[] args) throws Exception {

		// Twitter検索を利用して文書をクロールします。
		// https://developer.twitter.com
		TwitterCrawler crawler = new TwitterCrawler();
		{
			crawler.setProperty("oauth.consumerKey", "KoA3PhFy9A2BoRK1GFdAyw");
			crawler.setProperty("oauth.consumerSecret", "tIXrZji6mI4IF4BzhIdfC6R0Esqq0ZnrTF0cic6mgs");
			crawler.setProperty("oauth.accessToken", "34093079-7uD8Uzek0FFS57sYkE9lghAnYIx4lAEPviDMwo19W");
			crawler.setProperty("oauth.accessTokenSecret", "FcSY2QPXbQb4WPwKMRwlB8p0oeissf2z7hzLicaiqRvJ4");
			crawler.setProperty("query", "from:trumptrackerjp");
		}
		ArrayList<Document> docs = crawler.crawlDocuments();
	}

}

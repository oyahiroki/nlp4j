package nlp4j.webcrawler.caa.v202411;

import java.io.File;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;

import nlp4j.tuple.Pair;
import nlp4j.util.IOUtils;

public class CaaRecallCrawler202411Main2 {
	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) throws Exception {

		// https://www.recall.caa.go.jp/result/index.php?search=&viewCount=10&screenkbn=01&viewCountdden=10&portarorder=2&actionorder=0&pagingHidden=1

//		String url = "https://www.recall.caa.go.jp/result/detail.php?rcl=00000033030&screenkbn=01";

//		String url = "https://www.recall.caa.go.jp/result/detail.php?rcl=00000033029&screenkbn=01";

//		String url = "https://www.recall.caa.go.jp/result/detail.php?rcl=00000032937&screenkbn=01";
		String url = "https://www.recall.caa.go.jp/result/detail.php?rcl=00000033028&screenkbn=01";

		Pair<PrintWriter, File> p = IOUtils.pwTemp();

		JsonObject jo = CaaRecallCrawlerForContents.crawl(url);

		p.getLeft().println(jo.toString());

		p.getLeft().close();

		System.err.println(p.getRight().getAbsolutePath());

	}

}

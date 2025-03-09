package nlp4j.webcrawler.p202411ibmjpcareesfaq;

import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonObject;

import nlp4j.util.DateUtils;
import nlp4j.util.IOUtils;

public class IBMJPCarerrsFaqCrawler2 {

	public static void main(String[] args) throws Exception {

		PrintWriter pw = IOUtils.pw(
				"file/nlp4j.webcrawler.p202411ibmjpcareesfaq/ibmjpcareesfaq_" + DateUtils.get_yyyyMMdd() + "_json.txt");

		final String url = "https://www.ibm.com/jp-ja/careers/faq";

		org.jsoup.nodes.Document jsDoc = Jsoup.connect(url).userAgent("MyWebCrawler/1.0 (+http://nlp4j.org)").get();

		System.out.println(jsDoc);

		Elements els = jsDoc.select("cds-accordion-item");
		for (int n = 0; n < els.size(); n++) {
			{
				Element el = els.get(n);
				String q = el.attr("label");
				String a = el.select("div").get(0).text().trim();

				System.out.println("q: " + q);
				System.out.println("a: " + a);

				JsonObject jo = new JsonObject();
				jo.addProperty("id", (n + 1));
				jo.addProperty("url", url);
				jo.addProperty("text_ja_q", q);
				jo.addProperty("text_ja_a", a);
				jo.addProperty("text_ja", q + "\n" + a);

				pw.println(jo.toString());
				pw.flush();

			}
		}

		pw.close();

	}

}

// IBM はクライアントによって以下のようなレスポンスを返す
//<html>
//<head></head>
//<body>
// https://www.ibm.com/jp-ja/careers/faq
//</body>
//</html>
package nlp4j.webcrawler.p202411ibmjpcareesfaq;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class IBMJPCarerrsFaqCrawler2 {

	public static void main(String[] args) throws Exception {

		final String url = "https://www.ibm.com/jp-ja/careers/faq";

		org.jsoup.nodes.Document jsDoc = Jsoup
				.connect(url)
				.userAgent("MyWebCrawler/1.0 (+http://nlp4j.org)")
				.get();

		System.out.println(jsDoc);

		Elements els = jsDoc.select("caem-accordion-item");
		for (int n = 0; n < els.size(); n++) {
			{
				Element el = els.get(n);
				String q = el.attr("label");
				String a = el.select("div").get(0).text().trim();

				System.out.println("q: " + q);
				System.out.println("a: " + a);
			}
		}

	}

}

// IBM はクライアントによって以下のようなレスポンスを返す
//<html>
//<head></head>
//<body>
// https://www.ibm.com/jp-ja/careers/faq
//</body>
//</html>
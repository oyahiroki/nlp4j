package nlp4j.webcrawler.p202411ibmjpcareesfaq;

import java.io.File;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonObject;

import nlp4j.tuple.Pair;
import nlp4j.util.IOUtils;

public class IBMJPCarerrsFaqCrawler {

	public static void main(String[] args) throws Exception {

		Pair<PrintWriter, File> pf = IOUtils.pwTemp();
		PrintWriter pw = pf.getLeft();
		File file_out = pf.getRight();

		final String url = "https://www.ibm.com/jp-ja/careers/faq";

		final File file = new File("src/main/resources/nlp4j.ibmjpcareesfaq/page.html");
		org.jsoup.nodes.Document jsDoc = Jsoup.parse(file);
		Elements els = jsDoc.select("caem-accordion-item");
		for (int n = 0; n < els.size(); n++) {
			JsonObject jo = new JsonObject();
			{
				jo.addProperty("url", url);
			}
			{
				Element el = els.get(n);
				String q = el.attr("label");
				String a = el.select("div").get(0).text().trim();
				String text = "Q:" + q + "\n" + "A:" + a;
				jo.addProperty("text", text);
			}
			pw.println(jo.toString());
		}

		pw.close();

		System.err.println(file_out.getAbsolutePath());

	}

}

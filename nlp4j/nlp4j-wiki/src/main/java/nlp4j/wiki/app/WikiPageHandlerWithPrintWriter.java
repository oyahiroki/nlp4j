package nlp4j.wiki.app;

import java.io.PrintWriter;

import com.google.gson.JsonObject;

import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;

public class WikiPageHandlerWithPrintWriter implements WikiPageHandler {
	private int count = 0;
	private int count_max = -1;
	PrintWriter pw;

	public WikiPageHandlerWithPrintWriter(PrintWriter pw, int count_max) {
		this.pw = pw;
		this.count_max = count_max;
	}

	@Override
	public void read(WikiPage page) throws BreakException {

		if (page.getNamespaceAsInt() != 0) {
			return;
		}

		if (page.getTitle() != null && page.getTitle().startsWith("Wikipedia:")) {
			return;
		}

		JsonObject jo = new JsonObject();
		jo.addProperty("timestamp", page.getTimestamp());
		jo.addProperty("title", page.getTitle());
		jo.addProperty("text", page.getRootNodePlainText());

		if (pw != null) {
			pw.println(jo.toString());
		}
		count++;
		if (count % 100 == 0) {
			System.err.println("count: " + count);
		}
		if (count % 1000 == 0) {
			pw.flush();
		}
		if (count >= count_max) {
			throw new BreakException("count_max=" + count_max);
		}

	}

}

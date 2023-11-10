package nlp4j.wiki.converter;

import java.io.File;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;

import nlp4j.util.DateUtils;
import nlp4j.util.IOUtils;
import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;

public class MediaWikiJsonConverterType1 {

	public static void main(String[] args) throws Exception {

		boolean debug = false;

		String lang = "ja";
		String media = "wiki";
		String version = "20230101";

		// WIKI DUMP FILE
		String dumpFileName = "/usr/local/wiki/" //
				+ lang + media //
				+ "/" //
				+ version //
				+ "/" //
				+ lang + media //
				+ "-" //
				+ version //
				+ "-pages-articles-multistream.xml.bz2";

		String ts = DateUtils.get_yyyyMMdd_HHmmss();

		// WIKI DUMP FILE
		String outFileName = "/usr/local/wiki/" //
				+ lang + media //
				+ "/" //
				+ version //
				+ "/" //
				+ lang + media //
				+ "-" //
				+ version //
				+ "-pages-articles-multistream_type1_" + ts + ".jsonl";

		File dumpFile = new File(dumpFileName);
		File outFile = new File(outFileName);

		System.err.println(outFile.getAbsolutePath());

		try ( //

				WikiDumpReader dumpReader = new WikiDumpReader(dumpFile); //
				PrintWriter pw = debug ? IOUtils.pwSystemErr() : IOUtils.printWriter(outFile); //
		) {
			dumpReader.read(new WikiPageHandler() {
				int count = 0;

				@Override
				public void read(WikiPage page) throws BreakException {

					if (page.getTitle().contains(":")) {
						return; // skip
					}

					count++;

					if (count % 100 == 0) {
						System.err.println(count);
					}

					if (debug && count > 100) {
						throw new BreakException();
					}
//					if (count > 100) {
//						throw new BreakException();
//					}

					JsonObject jo = new JsonObject();

					jo.addProperty("id", page.getId());

					String url = "https://" + lang + ".wikipedia.org/wiki/"
							+ URLEncoder.encode(page.getTitle(), StandardCharsets.UTF_8);

					jo.addProperty("url", url);
					jo.addProperty("title", page.getTitle());

					jo.addProperty("text", page.getPlainText());

					pw.println(jo.toString());

//					System.err.println("id: " + page.getId());
//					System.err.println("url: " + "https://" + lang + ".wikipedia.org/wiki/"
//							+ URLEncoder.encode(page.getTitle(), StandardCharsets.UTF_8));
//					System.err.println("title: " + page.getTitle());
//					System.err.println("isRedirect: " + page.isRediect());
//					System.err.println("redirect_title: " + page.getRediect_title());
//
////					System.err.println(page.getPlainText().replace("\r", "").replace("\n", "").trim());
//
////					System.err.println(page.getText());
//					System.err.println("---");
				}
			});
		} catch (BreakException be) {
		}

		System.err.println(outFile.getAbsolutePath());

	}
}

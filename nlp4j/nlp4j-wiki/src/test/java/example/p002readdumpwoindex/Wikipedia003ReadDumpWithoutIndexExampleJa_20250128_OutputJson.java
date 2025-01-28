package example.p002readdumpwoindex;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.util.IOUtils;
import nlp4j.util.TextUtils;
import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;
import nlp4j.wiki.util.MediaWikiTextUtils;

public class Wikipedia003ReadDumpWithoutIndexExampleJa_20250128_OutputJson {

	public static void main(String[] args) throws Exception {

		// DUMP FILE NAME
		String dumpFileName = "/usr/local/wiki/jawiki/20250101/" + "jawiki-20250101-pages-articles-multistream.xml.bz2";

		String jsonOutFileName = "/usr/local/wiki/jawiki/20250101/" + "out_" + System.currentTimeMillis() + "_json.txt";

		String lang = "ja";
		String media = "wikipedia";

		// WIKI DUMP FILE
		File dumpFile = new File(dumpFileName);

		try ( //
				WikiDumpReader dumpReader = new WikiDumpReader(dumpFile); //
				PrintWriter pw = IOUtils.pw(jsonOutFileName); //
		) {
			dumpReader.read(
					// YOUR WikiPageHandelr HERE
					new WikiPageHandler() {
						int count = 0; // YOUR BREAK CONDITIN IF YOU NEED
						private int count_max = Integer.MAX_VALUE;

						@Override
						public void read(WikiPage page) throws BreakException {

							if (page.getTitle().contains(":")) {
								System.err.println("SKIP: " + page.getTitle());
								return; // SKIP Template page
							}

							count++;

							if (count > count_max) { // YOUR BREAK CONDITION HERE
								throw new BreakException();
							}

							if (count % 1000 == 0) {
								System.err.println("count: " + count);
							}

//							System.out.println("---");
//							System.out.println(page.getTitle());
//							System.out.println("---");
//							System.out.println(page.getText());

							JsonObject jo = new JsonObject();

							jo.addProperty("id", page.getId());
							jo.addProperty("title", page.getTitle());

//							System.out.println("---");
//							System.out.println("---");

							{
								// "https://ja.wikipedia.org/wiki?curid=" + id
								String url = "https://" + lang + "." + media + ".org/wiki?curid=" + page.getId();
//								System.out.println(url);
								jo.addProperty("url", url);
							}
							{
//								System.out.println(page.getPlainText());
								String text = (page != null && page.getPlainText() != null) // 20250128
										? page.getPlainText().replace("\r", "").trim() // 20250128
										: ""; // 20250128
								{
									int idx1 = text.indexOf("\n");
									if (idx1 != -1) {
										text = text.substring(0, idx1);
									} else {
//										System.err.println("SHORT: " + text);
//										System.err.println("---");
//										System.err.println("SHORT_TEXT: " + page.getText());
//										System.err.println("---");
										text = MediaWikiTextUtils.toPlainText(page.getTitle(),
												TextUtils.removeBrackets(page.getText()).trim());
//										System.err.println("SHORT_TEXT: " +  text);
//										System.err.println("---");
									}
//									System.out.println(text.substring(0, idx1));

									int idx2 = ((text == null) ? -1 : text.indexOf("\n"));
									if (idx2 != -1) {
										text = text.substring(0, idx2);
									}

									jo.addProperty("text_short", page.getTitle() + " " + text);
								}
							}

							{
								jo.addProperty("timestamp", page.getTimestamp());
							}
							{
								List<String> tags = MediaWikiTextUtils.parseCategoryTags(page.getText());
								if (tags != null) {
									JsonArray categoryTag = new JsonArray();
									for (String t : tags) {
										categoryTag.add(t);
									}
									jo.add("tags", categoryTag);
								}
							}

//							System.out.println(jo.toString());

							pw.println(jo.toString());

//							System.out.println("---");

						}
					});
		} catch (BreakException be) {
			System.err.println("OK");
		}

	}
}

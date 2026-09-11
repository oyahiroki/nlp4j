package nlp4j.wiki.app;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;

import com.google.gson.JsonObject;

import nlp4j.util.ArgUtils;
import nlp4j.util.DateUtils;
import nlp4j.util.IOUtils;
import nlp4j.util.JsonObjectUtils;
import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;
import nlp4j.wiki.util.MediaWikiTextUtils;

//--wikidump "/usr/local/wiki/jawiki/20260801/jawiki-20260801-pages-articles-multistream.xml.bz2"
//--wikidump-index "jawiki-20260801-pages-articles-multistream-index.txt.bz2"

public class WikiDumpToJsonForSearch {

	public static void main(String[] args) throws Exception {
		Map<String, String> options = ArgUtils.parseArgs(args);

		// 必須チェック
		if (!options.containsKey("wikidump") || !options.containsKey("out")) {
			printUsage();
			System.exit(1);
		}

		File dumpFile = new File(options.get("wikidump"));
		File fileOut = new File(options.get("out") + "." + DateUtils.get_yyyyMMdd_HHmmss() + ".jsonl");

		boolean filter = false;

		try (PrintWriter pw = IOUtils.printWriter(fileOut);) {

			// 自作のHandlerを指定する
			WikiPageHandler wikiPageHander = new WikiPageHandler() {
				private int count = 0;
				private int count_max = Integer.MAX_VALUE;

				@Override
				public void read(WikiPage page) throws BreakException {
//				System.err.println(page);
					if (page != null && page.getTitle().contains(":") == true) {
						// SKIP
//					System.err.println("SKIP: " + page.getTitle());
						return;
					} //
					else {

						List<String> categoryTags = MediaWikiTextUtils.parseCategoryTags(page.getXml());
						String title = page.getTitle();

						if (filter == true) {
							boolean isAnime = nlp4j.utils.ListUtils.matchesAny(".*漫画.*", categoryTags);
							if (isAnime == false) {
								return;
							}
						}

						String text_short = page.getRootNodePlainText().lines().findFirst().orElse("");
//						List<String> links = MediaWikiTextUtils.getWikiPageLinks(page.getXml());

						JsonObject jo = new JsonObject();
						{
							jo.addProperty("id", page.getId());
							jo.addProperty("title_s", title);
							jo.addProperty("text_ja", text_short);
							jo.addProperty("timestamp_dt", page.getTimestamp());
							jo.add("category_s", JsonObjectUtils.toJsonArray(categoryTags));
//							jo.add("links", JsonObjectUtils.toJsonArray(links));
						}
						pw.println(jo.toString());
						System.err.println(page.getTitle());
						System.err.println("---");

						count++;
						if (count > count_max) {
							throw new BreakException();
						}
					}
				}
			};

			try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile)) {
				try {
					dumpReader.read(wikiPageHander);
				} catch (BreakException be) {
					System.err.println("OK");
				}
			}
		}
	}

	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("  java -jar wikidump-sampler.jar --input <file> --count <number> [options]");
		System.out.println();
		System.out.println("Options:");
//		System.out.println("  --format <jsonl|csv>   Output format (default: jsonl)");
		System.out.println("  --output <file>        Output file (default: stdout)");
//		System.out.println("  --seed <number>        Random seed");
//		System.out.println("  --lang <code>          Language (e.g., ja, en)");
		System.out.println("  --filter <expr>        Filter condition");
	}

}

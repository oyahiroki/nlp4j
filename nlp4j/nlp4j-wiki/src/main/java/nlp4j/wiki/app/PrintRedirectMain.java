package nlp4j.wiki.app;

import java.io.File;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;

import nlp4j.util.IOUtils;
import nlp4j.util.TextUtils;
import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;

/**
 *
 */
public class PrintRedirectMain {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) throws Exception {

		if (args != null && args.length != 1) {
			System.err.println("args[0]: Full path of wikipedia dump "
					+ ": /usr/local/wiki/jawiki/20210401/jawiki-20210401-pages-articles-multistream.xml.bz2");
			return;
		}

		boolean debug = false;

		String dumpFileName = args[0];

		// WIKIPEDIA
		File dumpFile = new File(dumpFileName);

		if (dumpFile.exists() == false) {
			System.err.println("Not found: " + dumpFile.getAbsolutePath());
			return;
		}

		int idx = dumpFileName.lastIndexOf(".xml.bz2");
		String baseName = dumpFileName.substring(0, idx);

		// 出力ファイル
		String outputFile1 = baseName//
				+ "_nlp4j_redirects.txt";

		if ((new File(outputFile1)).exists()) {
			logger.info("exists: " + outputFile1);
			return;
		}

		PrintWriter pw_out_json = IOUtils.printWriter(outputFile1);

		// 自作のHandlerを指定する
		WikiPageHandler wikiPageHander = new WikiPageHandler() {
			int count = 0;

			@Override
			public void read(WikiPage page) throws BreakException {
				if (page.getNamespaceAsInt() != 0) {
					return; // SKIP
				}

				count++;
				if (count % 1000 == 0) {
					logger.info(count);
				}
				if (debug == true && count > 1000) {
					throw new BreakException();
				}

				String title = TextUtils.removeBrackets(page.getTitle()).trim(); // (曖昧さ回避) があるのでそのまま入れない

				// リダイレクト == 被リダイレクトだけを処理すればOK?
				boolean isredirected = false;
				String redirected_to = null;
				{
					if (page.getText().startsWith("#REDIRECT")
							// 日本語で「#転送 」というページもある。「水戸光圀」など
							|| page.getText().startsWith("#転送 ")) {
						isredirected = true;
					}
					if (isredirected == true) {
						List<String> wikilinks = page.getWikiLinks();
						if (wikilinks != null && wikilinks.size() == 1) {
							redirected_to = wikilinks.get(0);
							pw_out_json.println(title + "\t" + redirected_to);
//							logger.info("リダイレクト: " + page.getTitle() + " -> " + redirected_to);
						}
					}
				}

				boolean aimai = false;
				{
					if (page.getTitle().contains("曖昧さ回避") //
							|| String.join(",", page.getCategoryTags()).contains("曖昧さ回避") //
							|| page.getText().contains("{{aimai}}") //
					) {
						aimai = true;
					}
					if (aimai == true) {
//						System.err.println("曖昧さ回避");

//						List<String> wikilinks = page.getWikiLinks();
//						// aimai
////						jo.addProperty("aimai", true);
//
//						Set<String> wikilink_words = new LinkedHashSet<>();
//
//						wikilinks = wikilinks.stream() //
//								.filter(s -> (s.contains(":") == false)) //
//								.filter(s -> (s.contains("#") == false)) //
//								.toList();
//
//						if (wikilink_words.size() > 0) {
//							pw_out_json.println(title + "\t" + StringUtils.join(wikilink_words, ","));
//						}

						return;
					}
				}

				// {{aimai}}

			}
		};

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile)) {
			try {
				dumpReader.read(wikiPageHander);
			} catch (BreakException be) {
				System.err.println("Break");
			}
			System.err.println("Done");
		}

	}

}

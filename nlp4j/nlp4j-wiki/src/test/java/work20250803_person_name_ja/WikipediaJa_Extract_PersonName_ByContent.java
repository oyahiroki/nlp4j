package work20250803_person_name_ja;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.text.Normalizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.counter.Counter;
import nlp4j.tuple.Pair;
import nlp4j.util.IOUtils;
import nlp4j.util.TextUtils;
import nlp4j.util.UnicodeUtils;
import nlp4j.wiki.AbstractWikiPageHandler;
import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;

public class WikipediaJa_Extract_PersonName_ByContent {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) throws Exception {

//		String wiki_dir = "/usr/local/wiki/jawiki/20250801/";
		String wiki_dir = "r:/";
		String wiki_dumpFileName = wiki_dir + "jawiki-20250801-pages-articles-multistream.xml.bz2";

		// Create WikiPage handler
		WikiPageHandler wikiPageHander = new AbstractWikiPageHandler() {

			int count = 0;

//			Pair<PrintWriter, File> p = IOUtils.pwSysErrTemp();
			Pair<PrintWriter, File> p1 = IOUtils.pwTemp("wikipedia-ja-familyname", ".txt");
			Pair<PrintWriter, File> p2 = IOUtils.pwTemp("wikipedia-ja-fullname", ".txt");
			Counter<String> counter = new Counter<String>();

			@Override
			public void startDocument() {
				logger.info("startDocument()");
				System.err.println(p1.getRight().getAbsolutePath());
				System.err.println(p2.getRight().getAbsolutePath());
			}

			@Override
			public void endDocument() {
				finish();
			}

			private void finish() {
				counter.print();
				System.err.println(p1.getRight().getAbsolutePath());
				System.err.println(p2.getRight().getAbsolutePath());
				p1.getLeft().flush();
				p1.getLeft().close();
				p2.getLeft().flush();
				p2.getLeft().close();

				try {
					counter.print(IOUtils.pwTemp().getLeft());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void read(WikiPage page) throws BreakException {
//				System.err.println(p.getRight().getAbsolutePath());
//				System.err.println(page);
//				System.err.println(page.getNamespace());
				if (page != null && page.getNamespace().equals("0")) {
					count++;

					boolean isTarget = false;
					String family_name = null;
					// コンテンツのチェック（人物かどうかの判定）
					{
						for (String s : page.getText().split("\n")) {
							s = s.trim();
							if (s.startsWith("[[Category") && s.endsWith("人物]]")) {
								isTarget = true;
								break;
							}
							if (family_name == null) {
								if (s.startsWith("'''")) {
									int idx1 = s.indexOf(" ", 3);
									int idx2 = s.indexOf("'''", 3);
									if (idx1 != -1 && idx2 != -1 && (idx1 < idx2)) {
										family_name = s.substring(3, idx1).trim();
									} //
								}
							}

//							if (s.contains("[[Category:日本語の姓]]")) {
//								isTarget = true;
//								break;
//							}
						}
					}

					if (isTarget) {
						if (family_name != null) {
							String t = page.getTitle();
							t = TextUtils.nfkc(t);
							if (t.contains("(")) {
								int idx = t.indexOf("(");
								t = t.substring(0, idx).trim();
							}
//							System.err.println("title=" + t);
//							System.err.println("family_name?=" + family_name);
//							System.err.println(t);
							p1.getLeft().println(family_name);
							p1.getLeft().flush();
							p2.getLeft().println(t);
							p2.getLeft().flush();
							counter.add(family_name);
						}
					}
					if (count % 1000 == 0) {
						System.err.println(count);
					}
//					if (count > 10000) { // IF YOU WANT TO BREAK
//						finish();
//						throw new BreakException();
//					}
				} else {
					// do nothing
				} // END_OF_IF

//				System.err.println(p.getRight().getAbsolutePath());

			} // END_OF_read()
		}; // Handler

		try (WikiDumpReader dumpReader = new WikiDumpReader(wiki_dumpFileName)) {
			try {
				dumpReader.read(wikiPageHander);
			} catch (BreakException be) {
				// OK
			}
		}

	}
}

package work20250803_person_name_ja;

import java.io.File;
import java.io.PrintWriter;
import java.text.Normalizer;

import nlp4j.tuple.Pair;
import nlp4j.util.IOUtils;
import nlp4j.util.TextUtils;
import nlp4j.util.UnicodeUtils;
import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;

public class WikipediaJa_Extract_PersonName_ByCategory {

	public static void main(String[] args) throws Exception {

		String wiki_dir = "/usr/local/wiki/jawiki/20250801/";
		String wiki_dumpFileName = wiki_dir + "jawiki-20250801-pages-articles-multistream.xml.bz2";

		// Create WikiPage handler
		WikiPageHandler wikiPageHander = new WikiPageHandler() {
			int count = 0;

			Pair<PrintWriter, File> p = IOUtils.pwSysErrTemp();

			@Override
			public void read(WikiPage page) throws BreakException {
//				System.err.println(p.getRight().getAbsolutePath());
//				System.err.println(page);
//				System.err.println(page.getNamespace());
				if (page != null && page.getNamespace().equals("0")) {
					count++;

					boolean isTarget = false;
					// コンテンツのチェック（人物かどうかの判定）
					{
						for (String s : page.getText().split("\n")) {
							s = s.trim();
//							if (s.startsWith("[[Category") && s.endsWith("人物]]")) {
//								isTarget = true;
//								break;
//							}
							if (s.contains("[[Category:日本語の姓]]")) {
								isTarget = true;
								break;
							}
						}
					}

					if (isTarget) {
						String t = page.getTitle();
						t = TextUtils.nfkc(t);
						if (t.contains("(")) {
							int idx = t.indexOf("(");
							t = t.substring(0, idx).trim();
						}
//						System.err.println(t);
						p.getLeft().println(t);
						p.getLeft().flush();
					}
					if (count % 1000 == 0) {
						System.err.println(count);
					}
//					if (count > 10000) { // IF YOU WANT TO BREAK
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

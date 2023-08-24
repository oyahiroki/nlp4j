package nlp4j.data;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import nlp4j.Keyword;
import nlp4j.KeywordParser;
import nlp4j.sudachi.SudachiAnnotator;
import nlp4j.test.TestUtils;
import nlp4j.util.CsvWriter;
import nlp4j.util.IOUtils;
import nlp4j.util.TextUtils;
import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;

public class WikipediaJaDumpToDictionary_V2 {

	public static void main(String[] args) throws Exception {

		TestUtils.setLevelInfo();

		boolean debug = false;

		String lang = "ja";
		String media = "wiki";

		String dir = "/usr/local/wiki/" + lang + media + "/20230101/";

		String dumpFileName = dir + lang + media + "-20230101-pages-articles-multistream.xml.bz2";

		String out_dicFileName = dir + lang + media + "-20230101-pages-dic_v2.txt";

		PrintWriter pw = IOUtils.printWriter(out_dicFileName);

// 形態素解析1 (Aモード:Short)
		SudachiAnnotator annSudachi_A = new SudachiAnnotator();
		{
			annSudachi_A.setProperty("mode", "A");
			annSudachi_A.setProperty("target", "text");
			annSudachi_A.setProperty("pos", "(名詞)"); // (名詞|形容詞|動詞|接続詞|助詞|接尾辞|接尾辞|代名詞)
			annSudachi_A.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
		}
		SudachiAnnotator annSudachi_C = new SudachiAnnotator();
		{
			annSudachi_C.setProperty("mode", "C");
			annSudachi_C.setProperty("target", "text");
			annSudachi_C.setProperty("pos", "(名詞|固有名詞)"); // (名詞|形容詞|動詞|接続詞|助詞|接尾辞|接尾辞|代名詞)
			annSudachi_C.setProperty("systemDict", "/usr/local/sudachi/system_full.dic");
		}

// 形態素解析
		KeywordParser parser1_A = new KeywordParser(annSudachi_A); //
		KeywordParser parser1_C = new KeywordParser(annSudachi_C); //

		Set<String> entries = new HashSet<>();

		// Create WikiPage handler
		WikiPageHandler wikiPageHander = new WikiPageHandler() {
			int count = 0;

			@Override
			public void read(WikiPage page) throws BreakException {
				if (page != null && page.getNamespace().equals("0")) {
					count++;
					if (debug && count > 100) { // IF YOU WANT TO BREAK
						throw new BreakException();
					}
					if (count % 1000 == 0) {
						System.err.println(count);
					}

					if (page.getNamespaceAsInt() != 0) {
						return;
					}

					String id = page.getId();
					String title = page.getTitle();

					title = TextUtils.removeBrackets(title);
					title = title.trim();

					if (title.endsWith("一覧")) {
						return;
					}

					if (entries.contains(title)) {
						return;
					}

					if (StringUtils.isNumeric(title)) {
						System.err.println("SKIP: " + title);
						return;
					}

					List<Keyword> kwds;

					try {
						kwds = parser1_A.parse(title);
						int kw_size = (kwds != null) ? kwds.size() : 0;
						if (kw_size == 0) {
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}

					try {
						kwds = parser1_C.parse(title);
						int kw_size = (kwds != null) ? kwds.size() : 0;

						if (kw_size != 1) {
							return;
						}

						pw.println(title);
//						System.err.println(title);
						entries.add(title);
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}

				} else {
					// do nothing
				} // END_OF_IF
			} // END_OF_read()
		}; // Handler

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFileName)) {
			try {
				dumpReader.read(wikiPageHander);
			} catch (BreakException be) {
				// OK
			}
		}

		parser1_A.close();
		parser1_C.close();
		pw.close();

	}

}

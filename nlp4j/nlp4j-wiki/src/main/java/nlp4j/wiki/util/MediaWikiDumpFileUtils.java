package nlp4j.wiki.util;

import java.io.File;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.counter.Count;
import nlp4j.counter.Counter;
import nlp4j.util.JsonUtils;
import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.WikiPageHandler;

/**
 * created on: 2024-05-06
 * 
 * @since 1.1.0.0
 */
public class MediaWikiDumpFileUtils {

	/**
	 * @param mediaWikiDumpFile
	 * @throws IOException
	 * @since 1.1.0.0
	 */
	static public void printStats(File mediaWikiDumpFile) throws IOException {

		// 自作のHandlerを指定する DEFINE Wiki Dump Handler
		WikiPageHandler wikiPageHander = new WikiPageHandler() {
			int count = 0;
			Counter<String> counter = new Counter<String>();

			@Override
			public void read(WikiPage page) throws BreakException {
//				System.err.println(page);
				counter.add(page.getNamespace());
				if (page != null && page.getTitle().contains(":") == true) {
					// SKIP
//					System.err.println("SKIP: " + page.getTitle());
					return;
				} //
				else {
//					System.err.println("TITLE: " + page.getTitle());
//					System.err.println(page.getText());
//					{ // BREAK
//						throw new BreakException();
//					}
					count++;
					if (count % 1000 == 0) {
						System.out.println("count: " + count);
					}
				}
			}

			@Override
			public String toString() {
				JsonObject jo = new JsonObject();
				jo.addProperty("count", count);
				JsonArray namespaces = new JsonArray();
				for (Count<String> c : counter.getCountListSorted()) {
					JsonObject o = new JsonObject();
					o.addProperty(c.getValue(), c.getCount());
					namespaces.add(o);
				}
				jo.add("namespaces", namespaces);
				return JsonUtils.prettyPrint(jo);
			}

		};

		try (WikiDumpReader dumpReader = new WikiDumpReader(mediaWikiDumpFile)) {
			try {
				dumpReader.read(wikiPageHander);
			} catch (BreakException be) {
				System.err.println("OK");
			}
		}

		System.out.println("" + wikiPageHander.toString());

	}

}

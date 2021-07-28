package nlp4j.wiki;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import nlp4j.util.EnvUtil;
import nlp4j.util.StringUtils;

public class WikiMain {

	private static void print(String itemString) throws Exception {
		String indexFileName = EnvUtil.get("NLP4J_WIKI_INDEX");
		String dumpFileName = EnvUtil.get("NLP4J_WIKI_DUMP");

		File indexFile = new File(indexFileName);
		File dumpFile = new File(dumpFileName);

		try (WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile)) {
			WikiPage page = dumpReader.getItem(itemString);
			if (page == null) {
			} else {
				System.out.println(page.getText());
			}
		}
	}

	public static void main(String[] args) throws Exception {

		Class c = javax.xml.bind.PropertyException.class;
		System.err.println(EnvUtil.get("NLP4J_WIKI_DUMP"));
		System.err.println(EnvUtil.get("NLP4J_WIKI_INDEX"));

		if (EnvUtil.get("NLP4J_WIKI_INDEX") == null) {
			System.err.println("SET System ENV or Java Property: NLP4J_WIKI_INDEX={File path of WikiIndex}");
			return;
		}
		if (EnvUtil.get("NLP4J_WIKI_DUMP") == null) {
			System.err.println("SET System ENV or Java Property: NLP4J_WIKI_DUMP={File path of WikiDump}");
			return;
		}

		try (Scanner scanner = new Scanner(System.in)) {

			String s = scanner.nextLine();

			System.err.println(StringUtils.toHexUnicode(s));

//			s = new String(s.getBytes(), StandardCharsets.UTF_8);

			System.err.println(s);

			print(s);
		}

	}

}

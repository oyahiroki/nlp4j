package nlp4j.webcrawler.caa;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.util.ConsoleUtils;

public class CaaRecallDownloaderMain {

	private static final String HOW_TO_USE = CaaRecallDownloaderMain.class.getCanonicalName() //
			+ " -dir \"/usr/local/nlp4j/collections/caa/data/html\" -begin 0 -end 10000"; //
	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) {

		// -dir "/usr/local/nlp4j/collections/caa/data/html" -begin 0 -end 10000

		int begin = 0;
		int end = 10000;

		if (args.length != 6) {
			ConsoleUtils.printHowToUse(HOW_TO_USE, args);
			return;
		}

		Map<String, List<String>> params = ConsoleUtils.parseParams(args);

		if (params.get("-dir") == null || params.get("-dir").size() != 1) {
			ConsoleUtils.printHowToUse(HOW_TO_USE, args);
			return;
		}
		if (params.get("-begin") == null || params.get("-begin").size() != 1) {
			ConsoleUtils.printHowToUse(HOW_TO_USE, args);
			return;
		}
		if (params.get("-end") == null || params.get("-end").size() != 1) {
			ConsoleUtils.printHowToUse(HOW_TO_USE, args);
			return;
		}

		String dirName = params.get("-dir").get(0);

		try {
			begin = Integer.parseInt(params.get("-begin").get(0));
		} catch (NumberFormatException e) {
			ConsoleUtils.printHowToUse(HOW_TO_USE, args);
			return;
		}

		try {
			end = Integer.parseInt(params.get("-end").get(0));
		} catch (NumberFormatException e) {
			ConsoleUtils.printHowToUse(HOW_TO_USE, args);
			return;
		}

		CaaRecallDownloader crawler = new CaaRecallDownloader();
		crawler.setProperty("dir", dirName);
		crawler.setProperty("begin", "" + begin);
		crawler.setProperty("end", "" + end);
		crawler.crawlDocuments();
	}

}

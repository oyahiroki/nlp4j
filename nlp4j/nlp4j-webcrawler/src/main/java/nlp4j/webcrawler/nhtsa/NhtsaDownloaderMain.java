package nlp4j.webcrawler.nhtsa;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.util.ConsoleUtils;

/**
 * @author Hiroki Oya
 * @created_at 2021-05-01
 * @since 1.3.1.0
 */
public class NhtsaDownloaderMain {

	private static final String PARAM_DIR = "-dir";

	private static final String BASE_DATE_FORMAT = "yyyy-MM-dd";

	private static final String BASE_FILENAME = "FLAT_CMPL_%s.zip";

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private static final String HOW_TO_USE = NhtsaDownloaderMain.class.getCanonicalName() + " -dir {directory}";

	/**
	 * @param args : ["-dir","{output_directory}"]
	 * @throws Exception on Error
	 */
	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			ConsoleUtils.printHowToUse(HOW_TO_USE, args);
			return;
		}

		Map<String, List<String>> params = ConsoleUtils.parseParams(args);

		if (params.get(PARAM_DIR) == null || params.get(PARAM_DIR).size() != 1) {
			ConsoleUtils.printHowToUse(HOW_TO_USE, args);
			return;
		}

		String dirName = ".";

		dirName = params.get(PARAM_DIR).get(0);

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(BASE_DATE_FORMAT);
		String dt = sdf.format(date);

		String fileName = String.format(BASE_FILENAME, dt);

		File dir = new File(dirName);

		File outFile = new File(dir, fileName);

		NhtsaDownloader downloader = new NhtsaDownloader();
		downloader.setProperty("fileName", outFile.getAbsolutePath());

		logger.info("Started.");

		downloader.crawlDocuments();

		logger.info("Finished.");
	}

}

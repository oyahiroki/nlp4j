package nlp4j.wiki.util;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.webcrawler.utils.MD5Utils;

/**
 * created at: 2022-04-29
 * 
 * @author Hiroki Oya
 *
 */
public class MediaWikiMD5Util {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	static public boolean checkMD5(String md5sumsFileName, String targetFileName) throws IOException {

		return checkMD5(new File(md5sumsFileName), new File(targetFileName));

	}

	/**
	 * @param md5sumsFile MD5Sums file like
	 *                    https://dumps.wikimedia.org/jawiktionary/20220501/jawiktionary-20220501-md5sums.txt
	 * @param targetFile  like
	 *                    jawiktionary-20220501-pages-articles-multistream.xml.bz2
	 * @return result of MD5 check
	 * @throws IOException on IO Error
	 */
	static public boolean checkMD5(File md5sumsFile, File targetFile) throws IOException {

		Map<String, String> md5map = new HashMap<String, String>();

		List<String> lines = FileUtils.readLines(md5sumsFile, "UTF-8");
		for (String line : lines) {
			String[] ss = line.split("  ");
			String md5 = ss[0];
			String filename = ss[1];
			md5map.put(filename, md5);
		}

		String md5 = MD5Utils.md5(targetFile);

		String filename = targetFile.getName();

		if (md5map.get(filename) == null) {
			return false;
		} else {

			String md5_1 = md5map.get(filename);
			String md5_2 = md5;

			logger.info("Expected MD5=" + md5_1);
			logger.info("File     MD5=" + md5_2);

			return md5_1.equals(md5_2);
		}
	}

}

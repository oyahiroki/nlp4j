package nlp4j.crawler;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * ファイルクローラーの抽象クラス <br>
 * Abstract File Crawler
 * 
 * @author Hiroki Oya
 * @since 1.1.1.0
 *
 */
public abstract class AbstractFileCrawler extends AbstractCrawler implements Crawler {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	String encoding = "UTF-8";

	String filenameRegex = null; // = ".*\\.doc";

	ArrayList<File> files = new ArrayList<>();

	/**
	 * @param key   キー "file" | "dir" | "filename_ext"
	 * @param value 値
	 */
	@Override
	public void setProperty(String key, String value) {

		super.setProperty(key, value);

		logger.debug("key=" + key + ",value=" + value);

		if (key.equals("file")) {
			File file = new File(value);
			files.add(file);
		} //
		else if (key.equals("dir")) {
			File dir = new File(value);

			if (this.filenameRegex == null) {
				logger.warn("filenameRegex is not set.");
				files.addAll(Arrays.asList(dir.listFiles()));
			} //
			else {
				files.addAll(Arrays.asList(dir.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						if (name.matches(filenameRegex)) {
							return true;
						} else {
							return false;
						}
					}
				})));
			}

		} //
		else if (key.equals("filename_ext")) {
			filenameRegex = ".*\\." + value;
		} //

		else {

		}
	}

}

package nlp4j.wiki.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MediaWikiFileUtils {

	static public File getIndexFile(String dir, String language, String media, String version) throws IOException {
		String fileNamePostFix = "-pages-articles-multistream-index.txt.bz2";
		return getMediaFile(dir, language, media, version, fileNamePostFix);
	}

	static public File getDumpFile(String dir, String language, String media, String version) throws IOException {
		String fileNamePostFix = "-pages-articles-multistream.xml.bz2";
		return getMediaFile(dir, language, media, version, fileNamePostFix);
	}

	static public File getFile(String dir, String language, String media, String version, String type)
			throws IOException {
		if (type == null) {
			throw new FileNotFoundException("type is not set");
		} else if (type.equals("index")) {
			return getIndexFile(dir, language, media, version);
		} else if (type.equals("dump")) {
			return getDumpFile(dir, language, media, version);
		} else {
			throw new FileNotFoundException("type is not set");
		}
	}

	private static File getMediaFile(String dir, String language, String media, String version, String fileNamePostFix)
			throws IOException, FileNotFoundException {
		if ((new File(dir)).isDirectory() == false) {
			throw new IOException("Not directory: " + dir);
		} else {
			if (dir.endsWith("/") == false && dir.endsWith("\\") == false) {
				dir = dir + "/";
			}
		}
		String indexFileName = //
				String.format( //
						"%s" // dir
								+ "%s" // language;
								+ "%s" // media (wiktionary|wiki)
								+ "-" + "%s" // version
								+ fileNamePostFix,
						dir, language, media, version);

		File indexFile = new File(indexFileName);
		if (indexFile.exists() == false) {
			throw new FileNotFoundException("Not Found: " + indexFile.getAbsolutePath());
		} else {
			return indexFile;
		}
	}

}

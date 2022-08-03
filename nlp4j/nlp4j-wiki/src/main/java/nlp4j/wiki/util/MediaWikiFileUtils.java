package nlp4j.wiki.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * created at: 2022-04-29
 * 
 * @author Hiroki Oya
 *
 */
public class MediaWikiFileUtils {

	static public final String fileNamePostFixIndex = "-pages-articles-multistream-index.txt.bz2";
	static public final String fileNamePostFixDump = "-pages-articles-multistream.xml.bz2";

	// jawiki-20220501-abstract.xml
	static public final String fileNameAbstract = "%s%s-%s-abstract.xml.gz";

	/**
	 * Get index file of Media Wiki
	 * 
	 * @param dir      Directory of the file
	 * @param language Language of MediaWiki like "en", "ja"
	 * @param media    Media Type (wiktionary|wiki)
	 * @param version  version in yyyyMMdd format like "20220501"
	 * @return File of Index
	 * @throws IOException
	 */
	static public File getIndexFile(String dir, String language, String media, String version) throws IOException {
		return getMediaFile(dir, language, media, version, fileNamePostFixIndex);
	}

	/**
	 * Get dump file of Media Wiki
	 * 
	 * @param dir      Directory of the file
	 * @param language Language of MediaWiki like "en", "ja"
	 * @param media    Media Type (wiktionary|wiki)
	 * @param version  version in yyyyMMdd format like "20220501"
	 * @return File of Dump
	 * @throws IOException
	 */
	static public File getDumpFile(String dir, String language, String media, String version) throws IOException {
		return getMediaFile(dir, language, media, version, fileNamePostFixDump);
	}

	/**
	 * @param dir      Directory of the file
	 * @param language Language of MediaWiki like "en", "ja"
	 * @param media    Media Type (wiktionary|wiki)
	 * @param version  version in yyyyMMdd format like "20220501"
	 * @param type     File type (index|dump)
	 * @return
	 * @throws IOException
	 */
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

	/**
	 * <pre>
	 * Get abstract file
	 * Example: jawiki-20220501-abstract.xml.gz
	 * </pre>
	 * 
	 * @param dir
	 * @param language
	 * @param media
	 * @param version
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	static public File getAbstarctFile(String dir, String language, String media, String version)
			throws IOException, FileNotFoundException {
		String fileName = String.format(fileNameAbstract, language, media, version);
		File file = new File(dir, fileName);
		return file;
	}

	private static File getMediaFile(String dir, String language, String media, String version, String fileNamePostFix)
			throws IOException, FileNotFoundException {

		// IF(dir is not directory) THEN throw IOException
		if ((new File(dir)).isDirectory() == false) {
			throw new IOException("Not directory: " + dir);
		}
		// ELSE(dir is directory) THEN
		else {
			if (dir.endsWith("/") == false && dir.endsWith("\\") == false) {
				dir = dir + "/";
			}
		} // END OF IF

		String indexFileName = //
				String.format( //
						"%s" // dir
								+ "%s" // Language of MediaWiki like "en", "ja"
								+ "%s" // media (wiktionary|wiki)
								+ "-" + "%s" // version
								+ fileNamePostFix,
						dir, language, media, version);

		File indexFile = new File(indexFileName);

//		// IF(FILE NOT FOUND) THEN throw IOException
//		if (indexFile.exists() == false) {
//			throw new FileNotFoundException("Not Found: " + indexFile.getAbsolutePath());
//		}
//		// ELSE return file
//		else {
		return indexFile;
//		} // END OF IF
	}

}

package nlp4j.util;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author Hiroki Oya
 * @since 1.3.7.13
 *
 */
public class FilenameUtils {
	/**
	 * Convert URL to Filename
	 * 
	 * @param url
	 * @return
	 */
	static public String toFileName(URL url) {
		String fileName;
		try {
			fileName = URLEncoder.encode(url.toString(), "UTF-8");
//			fileName = fileName.replace(".", "%2E");
			return fileName;
		} catch (UnsupportedEncodingException e) {
			return null;
		}

	}
}

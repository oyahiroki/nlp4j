package nlp4j.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author Hiroki Oya
 * @since 1.1.0.0
 */
public class MD5Utils {

	/**
	 * Calculates the MD5 digest and returns the value as a 32 character hex string.
	 * 
	 * @param file
	 * @return MD5 digest as a hex string
	 * @throws IOException
	 */
	static public String md5(String fileName) throws IOException {
		return md5(new File(fileName));
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 32 character hex string.
	 * 
	 * @param file
	 * @return MD5 digest as a hex string
	 * @throws IOException
	 */
	static public String md5(File file) throws IOException {
		try (FileInputStream fis = new FileInputStream(file);) {
			String hexString = DigestUtils.md5Hex(fis);
			return hexString;
		}
	}

}

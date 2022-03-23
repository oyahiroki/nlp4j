package nlp4j.webcrawler.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {

	static public String md5(String fileName) throws IOException {
		return md5(new File(fileName));
	}

	static public String md5(File file) throws IOException {
		try (FileInputStream fis = new FileInputStream(file);) {
			String hexString = DigestUtils.md5Hex(fis);
			return hexString;
		}
	}

}

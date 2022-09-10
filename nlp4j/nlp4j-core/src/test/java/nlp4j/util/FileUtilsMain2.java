package nlp4j.util;

import java.io.File;
import java.io.IOException;

public class FileUtilsMain2 {

	public static void main(String[] args) {
		if (args.length != 1) {
			return;
		} else {
			File gzipFile = new File(args[0]);
			try {
				long count = FileUtils.lineCountGZipTextFile(gzipFile);
				System.err.println(count);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}

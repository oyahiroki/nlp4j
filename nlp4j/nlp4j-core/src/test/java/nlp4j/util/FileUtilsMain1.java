package nlp4j.util;

import java.io.File;
import java.io.IOException;

public class FileUtilsMain1 {

	public static void main(String[] args) {
		if (args.length != 1) {
			return;
		} else {
			File file = new File(args[0]);
			try {
				long count = FileUtils.lineCount(file);
				System.err.println(count);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}

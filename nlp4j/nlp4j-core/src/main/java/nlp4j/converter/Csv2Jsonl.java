package nlp4j.converter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import nlp4j.util.CsvUtils;
import nlp4j.util.DocumentUtil;
import nlp4j.util.IOUtils;

/**
 * @since 1.3.7.16
 */

public class Csv2Jsonl {

	static public void convert(File csv_file, File jsonl_file) throws IOException {

		try (PrintWriter pw = IOUtils.printWriter(jsonl_file);) {
			CsvUtils.stream(csv_file).forEach(d -> {
				pw.print(DocumentUtil.toJsonObject(d) + "\n");
			});
		}

	}

}

package nlp4j.util;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * created_at: 2022-12-31
 * 
 * @author Hiroki Oya
 *
 */
public class CsvUtils {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	static public List<List<String>> readCsv(File csvFile) throws IOException {
		String encoding = "UTF-8";
		CSVParser parser = CSVParser.parse(csvFile, //
				Charset.forName(encoding), //
				CSVFormat.EXCEL //
		);

		Iterable<CSVRecord> records = parser.getRecords();

		List<List<String>> ddd = new ArrayList<List<String>>();

		for (CSVRecord record : records) {
			List<String> dd = new ArrayList<String>();
			for (int n = 0; n < record.size(); n++) {
				String s = record.get(n);
				if (record.getRecordNumber() == 1 && s.startsWith(UnicodeUtils.BOM)) {
					s = UnicodeUtils.removeBOM(s);
					logger.info("removed BOM");
				}
				dd.add(s);
			}
			ddd.add(dd);
		}

		return ddd;

	}

}

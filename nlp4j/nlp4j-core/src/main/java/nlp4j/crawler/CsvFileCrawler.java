package nlp4j.crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;

import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.util.UnicodeUtils;

/**
 * CSVファイルをクロールします。<br>
 * Crawl documents from CSV
 * 
 * @author Hiroki Oya
 * @since 1.3.1.0
 *
 */
public class CsvFileCrawler extends AbstractFileCrawler implements Crawler {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * コンストラクタ <br>
	 * Default Constructor
	 */
	public CsvFileCrawler() {
		super();
		prop.setProperty("target", "text");
	}

	@Override
	public List<Document> crawlDocuments() {

		ArrayList<Document> docs = new ArrayList<>();

		String target = prop.getProperty("target");

		if (target == null) {
			logger.warn("target is not set.");
			return docs;
		}

		for (File file : super.files) {
			try (FileInputStream fis = new FileInputStream(file)) {
				docs.addAll(parseDocuments(fis));
			} catch (FileNotFoundException e1) {
				logger.error(e1.getMessage(), e1);
			} catch (IOException e1) {
				logger.error(e1.getMessage(), e1);
			}
		}
		return docs;
	}

	/**
	 * @param in
	 * @return parsed documents
	 * @throws IOException on IO Exception
	 * @since 1.3.1.0
	 */
	public List<Document> crawlDocuments(InputStream in) throws IOException {

		return parseDocuments(in);

	}

	private List<Document> parseDocuments(InputStream is) throws IOException {

		ArrayList<Document> docs = new ArrayList<>();

		@SuppressWarnings("deprecation")
		CSVParser parser = CSVParser.parse(is, //
				Charset.forName(super.encoding), //
				CSVFormat.EXCEL.withFirstRecordAsHeader() //
		);
		String[] headers = parser.getHeaderMap().keySet().toArray(new String[0]);
		if (headers.length > 0) {
			{
				String header0 = headers[0];
				if (header0.startsWith(UnicodeUtils.BOM)) {
					headers[0] = UnicodeUtils.removeBOM(header0);
					logger.info("removed BOM");
				}
			}
			for (int n = 0; n < headers.length; n++) {
				headers[n] = headers[n].trim();
			}
		}
		Iterable<CSVRecord> records = parser.getRecords();

		for (CSVRecord record : records) {

			Document doc = new DefaultDocument();
			{
				JsonArray data = new JsonArray();
				for (int n = 0; n < record.size(); n++) {
					String value = record.get(n);
					if (n == 0) {
						value = UnicodeUtils.removeBOM(value);
					}
					data.add(value);
				}
				doc.putAttribute("data", data);
			}
			{
				JsonArray header = new JsonArray();
				for (int n = 0; n < headers.length; n++) {
					String hd = headers[n];
					header.add(hd);
				}
				doc.putAttribute("header", header);
			}

			for (int n = 0; n < record.size(); n++) {
				String value = record.get(n);
				// <2022-12-31>
				// body data size is larger than header (short header)
				if (n < headers.length) {
					String header = headers[n].trim();
					if (n == 0) {
						header = UnicodeUtils.removeBOM(header);
					}
					if (header.trim().isEmpty()) {
						header = "header" + n;
					}
					doc.putAttribute(header, value);
				} //
				else {
					String header = "header" + n;
					doc.putAttribute(header, value);
				}
				// </2022-12-31>
			}
			docs.add(doc);
		}

		return docs;
	}
}

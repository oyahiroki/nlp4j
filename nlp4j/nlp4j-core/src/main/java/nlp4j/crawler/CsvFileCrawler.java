package nlp4j.crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;
import nlp4j.impl.DefaultDocument;

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
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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

		CSVParser parser = CSVParser.parse(is, //
				Charset.forName(super.encoding), //
				CSVFormat.EXCEL.withFirstRecordAsHeader() //
		);
		String[] headers = parser.getHeaderMap().keySet().toArray(new String[0]);
		Iterable<CSVRecord> records = parser.getRecords();

		for (CSVRecord record : records) {
			Document doc = new DefaultDocument();
			for (int n = 0; n < record.size(); n++) {
				String value = record.get(n);
				doc.putAttribute(headers[n], value);
			}
			docs.add(doc);
		}

		return docs;
	}
}

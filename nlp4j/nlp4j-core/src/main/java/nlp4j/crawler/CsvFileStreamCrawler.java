package nlp4j.crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.GZIPInputStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;

import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.util.IOUtils;
import nlp4j.util.UnicodeUtils;

public class CsvFileStreamCrawler extends AbstractFileCrawler implements Crawler, StreamDocumentCrawler {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public CsvFileStreamCrawler() {
		super();
	}

	@Override
	public List<Document> crawlDocuments() {
		try {
			Stream<Document> st = this.streamDocuments();
			return st.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<Document>();
		}
	}

	/**
	 * ファイルを順次読み込みながら、Document を Stream で返すメソッド
	 * 
	 * @return Stream of Document
	 */
	@Override
	public Stream<Document> streamDocuments() throws IOException {

		// Stream of Document を返す処理
		return super.files.stream().flatMap(file -> {
			try {
				return streamDocuments(new FileInputStream(file));
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				return Stream.empty(); // エラー時には空のストリームを返す
			}
		});
	}

	/**
	 * @param csvFile_or_gzipCsvFile
	 * @return
	 * @throws IOException
	 */
	public Stream<Document> streamDocuments(File file) throws IOException {
		InputStream in = IOUtils.inputStream(file);
		try {
			return streamDocuments(in);
		} catch (Exception e) {
			in.close();
			throw e;
		}
	}

	public Stream<Document> streamDocuments(InputStream in) throws IOException {

		CSVParser parser = CSVParser.parse(in, Charset.forName(encoding), //
				CSVFormat.EXCEL.withFirstRecordAsHeader());

		String[] headers = parser.getHeaderMap().keySet().toArray(new String[0]);

		if (headers.length > 0) {

			String header0 = headers[0];

			if (header0.startsWith(UnicodeUtils.BOM)) {
				headers[0] = UnicodeUtils.removeBOM(header0);
				logger.info("removed BOM");
			}

			for (int n = 0; n < headers.length; n++) {
				headers[n] = headers[n].trim();
			}
		}

		return StreamSupport.stream(parser.spliterator(), false).map(record -> {

			Document doc = new DefaultDocument();

			for (int n = 0; n < record.size(); n++) {
				String key = headers[n];
				String value = record.get(n);
				doc.putAttribute(key, value);
			}

			JsonArray header = new JsonArray();

			for (String hd : headers) {
				header.add(hd);
			}

			doc.putAttribute("_header", header);

			return doc;
		}).onClose(() -> {
			try {
				parser.close();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});
	}

	public Stream<Document> streamDocuments(URL url) throws IOException {

		if (url.toString().endsWith(".gz")) { // 1.3.7.18
			try (InputStream is = new GZIPInputStream(url.openStream())) { // 1.3.7.18
				return streamDocuments(is); // 1.3.7.18
			} // 1.3.7.18
		} //
		else {
			try (InputStream is = url.openStream()) {
				return streamDocuments(is);
			}
		}
	}

	public Stream<Document> streamDocumentsResource(String resourceName) throws IOException {

		InputStream in = CsvFileStreamCrawler.class.getClassLoader().getResourceAsStream(resourceName);

		if (in == null) {
			throw new IOException("Resource not found: " + resourceName);
		}

		try {
			if (resourceName.endsWith(".gz")) {
				return streamDocuments(new GZIPInputStream(in));
			}

			return streamDocuments(in);

		} catch (Exception e) {
			in.close();
			throw e;
		}
	}

}

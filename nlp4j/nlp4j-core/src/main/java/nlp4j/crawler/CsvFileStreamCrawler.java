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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;

import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.util.UnicodeUtils;

public class CsvFileStreamCrawler extends AbstractFileCrawler implements Crawler, StreamDocumentCrawler {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	public CsvFileStreamCrawler() {
		super();
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

	public Stream<Document> streamDocuments(File csvFile) throws IOException {
		try (InputStream is = new FileInputStream(csvFile)) {
			return streamDocuments(is);
		}
	}

	public Stream<Document> streamDocuments(URL url) throws IOException {
		try (InputStream is = url.openStream()) {
			return streamDocuments(is);
		}
	}

	/**
	 * InputStream から逐次的に Document を読み込み、Stream として返すメソッド
	 * 
	 * @param in InputStream
	 * @return Stream of Document
	 * @throws IOException on IO Exception
	 */
	private Stream<Document> streamDocuments(InputStream in) throws IOException {

		// CSVParser を使って CSV を読み込む
		CSVParser parser = CSVParser.parse(in, //
				Charset.forName(super.encoding), //
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

		// Iterable から Stream を生成する
		Iterable<CSVRecord> records = parser.getRecords();

		return StreamSupport.stream(records.spliterator(), false) //
				.onClose(() -> nocheckClose(in)) //
				.map(record -> {
					// Document の作成
					Document doc = new DefaultDocument();
					
					{
						for (int n = 0; n < record.size(); n++) {
							String key = headers[n];
							String value = record.get(n);
							doc.putAttribute(key, value);
						}
					}
					
//					{
//						JsonArray data = new JsonArray();
//						for (int n = 0; n < record.size(); n++) {
//							String value = record.get(n);
//							if (n == 0) {
//								value = UnicodeUtils.removeBOM(value);
//							}
//							data.add(value);
//						}
//						doc.putAttribute("data", data);
//					}
					{
						JsonArray header = new JsonArray();
						for (int n = 0; n < headers.length; n++) {
							String hd = headers[n];
							header.add(hd);
						}
						doc.putAttribute("_header", header);
					}

//					for (int n = 0; n < record.size(); n++) {
//						String value = record.get(n);
//						// header とデータを対応させる
//						if (n < headers.length) {
//							String header = headers[n].trim();
//							if (n == 0) {
//								header = UnicodeUtils.removeBOM(header);
//							}
//							if (header.trim().isEmpty()) {
//								header = "header" + n;
//							}
//							doc.putAttribute(header, value);
//						} else {
//							String header = "header" + n;
//							doc.putAttribute(header, value);
//						}
//					}
					return doc;
				});
	}

	private void nocheckClose(InputStream in) {
		try {
			in.close();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
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

}

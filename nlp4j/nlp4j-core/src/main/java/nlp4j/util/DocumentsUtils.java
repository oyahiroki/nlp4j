package nlp4j.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import nlp4j.Document;

/**
 * created_at: 2022-12-03
 * 
 * @author Hiroki Oya
 * @since 1.3.7.4
 */
public class DocumentsUtils {

	/**
	 * Write documents as CSV. Keywords are not output.
	 * 
	 * @param docs
	 * @param appendable
	 * @throws IOException
	 */
	static public void printAsCsv(List<Document> docs, Appendable appendable) throws IOException {

		List<String> header = new ArrayList<String>();

		for (Document doc : docs) {
			List<String> keys = doc.getAttributeKeys();
			for (String key : keys) {
				if (header.contains(key) == false) {
					header.add(key);
				}
			}
		}

		try (CSVPrinter printer = new CSVPrinter( //
				appendable, //
				CSVFormat.RFC4180.builder().setHeader(header.toArray(new String[0])).build() //
		);) {
			for (Document doc : docs) {
				List<String> record = new ArrayList<>();
				for (String h : header) {
					Object o = doc.getAttribute(h);
					if (o == null) {
						record.add("");
					} //
					else {
						record.add(o.toString());
					}
				}
				printer.printRecord(record);
			}
		}

	}

	/**
	 * Write documents as CSV. Keywords are not output.
	 * 
	 * @param docs
	 * @param file
	 * @throws IOException
	 */
	static public void printAsCsv(List<Document> docs, File file) throws IOException {
		printAsCsv(docs, file, "UTF-8");
	}

	/**
	 * Write documents as CSV. Keywords are not output.
	 * 
	 * @param docs
	 * @param file
	 * @param encoding
	 * @throws IOException
	 */
	static public void printAsCsv(List<Document> docs, File file, String encoding) throws IOException {
		Appendable a = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
		printAsCsv(docs, a);
	}

}

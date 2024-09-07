package nlp4j.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import nlp4j.Document;
import nlp4j.util.DocumentUtil;

/**
 * created on: 2024-08-25
 * 
 * @since 1.3.7.14
 */
public class JsonFileWriter implements AutoCloseable {

	/**
	 * Writes a list of Document objects to a specified file as JSON strings, each
	 * on a new line. This method ensures that the parent directory of the output
	 * file exists, creating it if necessary. Each Document in the provided list is
	 * converted to a JSON string using a utility method. These JSON strings are
	 * then written to the file, with each string followed by a newline character to
	 * separate the documents clearly.
	 * 
	 * @param outFile
	 * @param docs
	 * @throws IOException
	 * @since 1.3.7.14
	 */
	static public void write(File outFile, List<Document> docs) throws IOException {
		if (outFile.getParentFile().exists() == false) {
			FileUtils.forceMkdir(outFile.getParentFile());
		}
		// 各DocumentをJSONに変換してファイルに書き出す
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
			for (Document doc : docs) {
				String json = DocumentUtil.toJsonString(doc);
				writer.write(json);
				writer.newLine(); // 改行を挿入
			}
		}
	}

	private BufferedWriter writer;

	public JsonFileWriter(File outFile) {
		super();
		try {
			this.writer = new BufferedWriter(new FileWriter(outFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(Document doc) throws IOException {
		String json = DocumentUtil.toJsonString(doc);
		writer.write(json);
		writer.newLine(); // 改行を挿入
	}

	@Override
	public void close() throws Exception {
		if (this.writer != null) {
			writer.close();
		}
	}

}

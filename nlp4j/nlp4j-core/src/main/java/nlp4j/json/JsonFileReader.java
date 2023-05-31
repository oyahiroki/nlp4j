package nlp4j.json;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * <pre>
 * Read New Line Separated JSON TEXT FILE
 * 改行区切りのJSONファイルを読み込む
 * </pre>
 * 
 * created on: 2022-11-19
 * 
 * @author Hiroki Oya
 * @since 1.3.7.4
 *
 */
public class JsonFileReader implements Closeable {

	private int countLine = 0;
	private int maxLines = -1;
	private BufferedReader br;

	public JsonFileReader(File jsonFile) throws IOException {
		init(jsonFile, -1);
	}

	/**
	 * @param jsonFile
	 * @param maxLines 最大読み込み行数
	 * @throws IOException
	 */
	public JsonFileReader(File jsonFile, int maxLines) throws IOException {
		init(jsonFile, maxLines);
	}

	private void init(File jsonFile, int maxLines) throws FileNotFoundException, IOException {
		this.maxLines = maxLines;
		if (jsonFile.exists() == false) {
			throw new FileNotFoundException(jsonFile.getAbsolutePath());
		}

		// OPEN AS ZGIP
		if (jsonFile.getAbsolutePath().endsWith(".gz")) {
			br = new BufferedReader( //
					new InputStreamReader( //
							new GZIPInputStream( //
									new FileInputStream(jsonFile)),
							StandardCharsets.UTF_8));
		}
		// OPEN AS PLAIN TEXT
		else {
			br = new BufferedReader( //
					new InputStreamReader( //
							new FileInputStream(jsonFile), StandardCharsets.UTF_8));
		}
	}

	@Override
	public void close() throws IOException {
		if (br != null) {
			br.close();
		}
	}

	public JsonObject next() throws IOException {

		if ((maxLines > 0) && (countLine >= maxLines)) {
			return null;
		}

		String line = br.readLine();
		countLine++;

		if (line == null) {
			return null;
		} else {
			JsonObject jsonObject = (new Gson()).fromJson(line, JsonObject.class);
			return jsonObject;
		}

	}

	public void setMaxLines(int maxLines) {
		this.maxLines = maxLines;
	}

}

package nlp4j.json;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import nlp4j.util.FileUtils;

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
			FileUtils.checExists(jsonFile);
			throw new FileNotFoundException(jsonFile.getAbsolutePath());
		}

		this.br = nlp4j.util.IOUtils.bufferedReader(jsonFile);

	}

	@Override
	public void close() throws IOException {
		if (br != null) {
			br.close();
		}
	}

	public JsonObject next() throws IOException {

		if ((this.maxLines > 0) && (this.countLine >= this.maxLines)) {
			return null;
		}

		String line = br.readLine();
		this.countLine++;

		if (line == null) {
			return null;
		} else {
			try {
				JsonObject jsonObject = (new Gson()).fromJson(line, JsonObject.class); // throws JsonSyntaxException
				return jsonObject;
			} catch (JsonSyntaxException e) {
				throw new IOException(e);
			}
		}

	}

	/**
	 * Returns a Stream of JsonObjects read from the file.
	 * 
	 * @return Stream<JsonObject>
	 */
	public Stream<JsonObject> jsonObjectStream() {
		return br.lines() // Stream<String>
				.limit(this.maxLines > 0 ? this.maxLines : Long.MAX_VALUE) // Limit the stream to maxLines if specified
				.map(line -> {
					try {
						return (new Gson()).fromJson(line, JsonObject.class); // Convert each line to JsonObject
					} catch (JsonSyntaxException e) {
						throw new RuntimeException(new IOException(e)); // Wrap the exception
					}
				});
	}

	public void setMaxLines(int maxLines) {
		this.maxLines = maxLines;
	}

}

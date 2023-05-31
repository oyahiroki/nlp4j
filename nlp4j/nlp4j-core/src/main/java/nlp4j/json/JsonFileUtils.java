package nlp4j.json;

import java.io.File;
import java.io.IOException;

import com.google.gson.JsonObject;

import nlp4j.util.JsonUtils;

/**
 * created on: 2023-05-29
 * 
 * @author Hiroki Oya
 *
 */
public class JsonFileUtils {

	static public void print(String jsonFileName, int head) throws IOException {
		print(new File(jsonFileName), head);
	}

	static public void print(File jsonFile, int head) throws IOException {
		try ( //
				JsonFileReader jfr = new JsonFileReader(jsonFile, head); //
		) {
			JsonObject jo;
			while (((jo = jfr.next()) != null)) {
				System.out.println(JsonUtils.prettyPrint(jo));
			}
		} // END_OF (READ JSON)
		catch (Exception e) {
			throw new IOException(e);
		}

	}

}

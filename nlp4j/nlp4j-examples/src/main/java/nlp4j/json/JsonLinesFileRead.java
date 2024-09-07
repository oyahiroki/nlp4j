package nlp4j.json;

import java.io.File;
import java.io.IOException;

import com.google.gson.JsonObject;

public class JsonLinesFileRead {

	// Main method where the program starts execution
	public static void main(String[] args) throws IOException {
		// Specify the path of the gzipped JSON Lines file
		File jsonFile = new File("src/main/resources/nlp4j/jsonlines.gz");

		// Try-with-resources statement to automatically close the reader after use
		try (JsonFileReader reader = new JsonFileReader(jsonFile);) {
			JsonObject jo;

			// Loop through the file and read each JSON object until null (end of file)
			while ((jo = reader.next()) != null) {
				// Print the JSON object to the error output stream (System.err)
				System.err.println(jo.toString());

				/*
				 * Sample output (as shown in the comment):
				 * {"key1-1":"value1-1","key1-2":"value1-2","key1-3":"value1-3"}
				 * {"key2-1":"value2-1","key2-2":"value2-2","key2-3":"value2-3"}
				 * {"key3-1":"value3-1","key3-2":"value3-2","key3-3":"value3-3"} Each JSON
				 * object is printed to the console as it is read from the file.
				 */
			}
		}

	}

}

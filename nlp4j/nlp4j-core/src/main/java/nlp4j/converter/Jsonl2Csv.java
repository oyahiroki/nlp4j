package nlp4j.converter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import nlp4j.json.JsonFileReader;

/**
 * @since 1.3.7.16
 */
public class Jsonl2Csv {

	static public void convert(File jsonl_file, File csv_file) throws IOException {

		List<String> headers = new ArrayList<String>();

		try (JsonFileReader reader = new JsonFileReader(jsonl_file);) {
			JsonObject jo;

			// Loop through the file and read each JSON object until null (end of file)
			while ((jo = reader.next()) != null) {
				Set<String> keys = jo.keySet();
				keys.forEach(k -> {
					if (headers.contains(k) == false) {
						headers.add(k);
					}
				});
			}
		}

		try ( //
				JsonFileReader reader = new JsonFileReader(jsonl_file); //
				CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(new FileOutputStream(csv_file), "UTF-8"),
						CSVFormat.EXCEL.withHeader(headers.toArray(new String[0]))); //
		) {
			JsonObject jo;

			// Loop through the file and read each JSON object until null (end of file)
			while ((jo = reader.next()) != null) {
				List<String> data = new ArrayList<String>();
				for (String h : headers) {
					String d;
					JsonElement je = jo.get(h);
					if (je == null) {
						d = "";
					} //
					else if (je.isJsonPrimitive()) {
						JsonPrimitive jp = je.getAsJsonPrimitive();
						if (jp.isString()) {
							d = jp.getAsString();
						} //
						else if (jp.isNumber()) {
							d = jp.getAsNumber().toString();
						} //
						else {
							d = jp.toString();
						}
					} //
					else {
						d = je.toString();
					}
					data.add(d);
				}
				printer.printRecord(data);
			}
		}

	}

}

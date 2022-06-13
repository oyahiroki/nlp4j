package nlp4j.wiki;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import nlp4j.util.JsonUtils;

public class CirrussearchReader {

	public static void main(String[] args) {

		String path = "C:/usr/local/data/wiki/20220501/" + "jawiki-20220502-cirrussearch-content.json.gz";

		Path p = Paths.get(path);

		Gson gson = new Gson();

		File outFile = new File("/usr/local/data/wiki/wiki_ibm_temp.txt");

		try (InputStream is = Files.newInputStream(p);
				GZIPInputStream gzis = new GZIPInputStream(is);
				InputStreamReader isr = new InputStreamReader(gzis, StandardCharsets.UTF_8);
				BufferedReader br = new BufferedReader(isr);) {

			int count = 0;

			String s;
			while ((s = br.readLine()) != null) {
				count++;

				if (count % 2 == 1) {
					continue;
				}

//				System.err.println(s);

				JsonObject jsonObject = gson.fromJson(s, JsonObject.class);

//				String att = "opening_text";
				String att = "text";

				if (jsonObject.get(att) != null && jsonObject.get(att).isJsonNull() == false) {

					String text = jsonObject.get(att).getAsString();

					text = Normalizer.normalize(text, Form.NFKC);

					if (text.contains("IBM")) {
						System.err.print(".");
//						System.err.println(text);
						FileUtils.write(outFile, text + "\n", "UTF-8", true);
//						System.err.println(JsonUtils.prettyPrint(s));
					}

				}

//				if (count > 1000 * 1000) {
//					break;
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

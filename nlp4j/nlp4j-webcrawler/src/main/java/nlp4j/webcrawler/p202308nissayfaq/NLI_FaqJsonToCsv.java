package nlp4j.webcrawler.p202308nissayfaq;

import java.io.File;

import com.google.gson.JsonObject;

import nlp4j.json.JsonFileReader;
import nlp4j.util.CsvWriter;

public class NLI_FaqJsonToCsv {

	public static void main(String[] args) throws Exception {

		String dir = "file/nlp4j.webcrawler.nissayfaq/";
		String filename = dir + "nlp4j_webcrawler_nissay_faq_1_10013715040321952746974.txt";

		JsonFileReader r = new JsonFileReader(new File(filename), -1);
		JsonObject jo;

		String outFileName = dir + "nlp4j_webcrawler_nissay_faq_1_10013715040321952746974.csv";

		try (CsvWriter csvWriter = new CsvWriter(new File(outFileName));) {

			csvWriter.write("id", "url", "bread_list", "q", "a_text");

			while ((jo = r.next()) != null) {
				if (jo.get("response").getAsInt() != 200) {
					continue;
				}

				jo.remove("a_html");

				System.err.println(jo.get("id").getAsString());
				System.err.println(jo.get("url").getAsString());
				System.err.println(jo.get("bread_list").getAsString());
				System.err.println(jo.get("q").getAsString());
				System.err.println(jo.get("a_text").getAsString());

				System.err.println(jo.keySet());

				csvWriter.write( //
						jo.get("id").getAsString(), //
						jo.get("url").getAsString(), //
						jo.get("bread_list").getAsString(), //
						jo.get("q").getAsString(), //
						jo.get("a_text").getAsString() //
				);

			}
		}

	}

}

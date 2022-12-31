package nlp4j.komeda;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.crawler.CsvFileCrawler;
import nlp4j.importer.JsonOutImporter;
import nlp4j.util.DocumentUtil;
import nlp4j.util.DocumentsUtils;
import nlp4j.util.JsonUtils;

public class Komeda2023 {

	public static void main(String[] args) throws Exception {

		CsvFileCrawler cralwer = new CsvFileCrawler();
		{
			cralwer.setProperty("file", "file/komeda/komeda2023_bom_v2.csv");
		}

		List<Document> docs = cralwer.crawlDocuments();

		System.err.println(docs.size());

		String todofuken = "";

		for (int n = 0; n < docs.size(); n++) {
			Document doc = docs.get(n);
			String key = "都道府県";
//			System.err.println(doc.getAttributeAsString(key));
			if (doc.getAttributeAsString(key) != null //
					&& doc.getAttributeAsString(key).isEmpty() == false) {
				todofuken = doc.getAttributeAsString(key);
			} else {
//				System.err.println("OK");
				doc.putAttribute(key, todofuken);
			}
		}

		System.err.println(DocumentUtil.toPrettyJsonString(docs.get(0)));
		System.err.println(DocumentUtil.toPrettyJsonString(docs.get(1)));

//		JsonOutImporter importer = new JsonOutImporter();
//		importer.setProperty("file", "file/komeda/komeda2023_out.txt");
//		importer.importDocuments(docs);

		JsonArray jsonArray = new JsonArray();
		for (int n = 0; n < docs.size(); n++) {
			JsonObject obj = DocumentUtil.toJsonObject(docs.get(n));
			jsonArray.add(obj);
		}

		FileUtils.write(new File("file/komeda/komed2023_out2_json.txt"), jsonArray.toString(), "UTF-8", false);

	}

}

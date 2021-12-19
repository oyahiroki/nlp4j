package nlp4j.importer.examples;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import nlp4j.Document;
import nlp4j.importer.CsvOutImporter;
import nlp4j.util.DocumentUtil;

public class CsvOutImporterExample {

	public static void main(String[] args) throws Exception {

		List<Document> docs = new ArrayList<>();
		{
			docs.add(DocumentUtil
					.parseFromJson("{'id':'001','date':'2021-04-01T00:00:00Z','item':'aaa','text':'これはテストです．1'}"));
			docs.add(DocumentUtil.parseFromJson(
					"{'id':'002','date':'2021-04-02T00:00:00Z','item':'bbb','text':'これはテストです．カンマ(,)を入れます．2'}"));
			docs.add(DocumentUtil
					.parseFromJson("{'id':'003','date':'2021-04-03T00:00:00Z','item':'ccc','text':'これはテストです．3'}"));
		}

		File tempFile = File.createTempFile("nlp4j", ".csv");

		CsvOutImporter importer = new CsvOutImporter();
		{
			importer.setProperty("file", tempFile.getAbsolutePath());
			importer.setProperty("encoding", "UTF-8");
			importer.importDocuments(docs);
			importer.commit();
			importer.close();
		}

		String data = FileUtils.readFileToString(tempFile, "UTF-8");
		System.err.println(data);

		tempFile.deleteOnExit();
	}

}

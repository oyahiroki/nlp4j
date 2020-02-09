package example;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.impl.DefaultDocument;
import nlp4j.krmj.annotator.KuromojiAnnotator;
import nlp4j.util.DocumentUtil;

public class Main1CreateAnnotatedJson {

	public static void main(String[] args) throws Exception {

//		String fileName = "src/test/resources/file/neko_short_utf8.txt";
		String fileName = "src/test/resources/file/neko.txt";

		File inFile = new File(fileName);

		System.err.println("debug: inFile: " + inFile.getAbsolutePath());

		List<String> lines = FileUtils.readLines(inFile, "UTF-8");

		ArrayList<Document> docs = new ArrayList<Document>();

		for (String line : lines) {
			if (line.length() > 8) {
				Document doc = new DefaultDocument();
				doc.setText(line.replace("\u3000", " ").trim());
				docs.add(doc);
			} else {

			}
		}

		DocumentAnnotator annotator = new KuromojiAnnotator(); // 形態素解析
		annotator.setProperty("target", "text");

		{
			System.err.println("形態素解析");
			long time1 = System.currentTimeMillis();

			int n1 = docs.size();

			int n2 = 0;

			for (Document doc : docs) {
				n2++;
				// 形態素解析
				annotator.annotate(doc);
				System.err.println((double) n2 / (double) n1);
			}

			long time2 = System.currentTimeMillis();
			System.err.println("処理時間[ms]：" + (time2 - time1));
		}

		File file = new File("src/test/resources/file/neko_annotated_json.txt");

		DocumentUtil.writeAsLineSeparatedJson(docs, file);

	}

}

package nlp4j.ibm.wex;

import nlp4j.Document;
import nlp4j.DocumentBuilder;
import nlp4j.util.DocumentUtil;

public class WexKeywordAnnotatorTestMain {

	public static void main(String[] args) throws Exception {
		{
			Document doc = (new DocumentBuilder()).text("今日はいい天気だったので私は歩いて学校に行きました。").build();
			System.out.println(DocumentUtil.toJsonPrettyString(doc));
			WexKeywordAnnotator ann = new WexKeywordAnnotator();
			{
				ann.setProperty("target", "text");
			}
			ann.annotate(doc);
			System.out.println(DocumentUtil.toJsonPrettyString(doc));
		}
		{
			Document doc = (new DocumentBuilder()).text("2024年1月1日").build();
			System.out.println(DocumentUtil.toJsonPrettyString(doc));
			WexKeywordAnnotator ann = new WexKeywordAnnotator();
			{
				ann.setProperty("target", "text");
			}
			ann.annotate(doc);
			System.out.println(DocumentUtil.toJsonPrettyString(doc));
		}

	}

}

package nlp4j.okt.annotator;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.DocumentBuilder;
import nlp4j.Keyword;
import nlp4j.util.DocumentUtil;

public class OktAnnotatorTestCase extends TestCase {

	public void testAnnotateDocument001() throws Exception {

		// 今日は天気がよいので学校に歩いて行った
		String text = "오늘은 날씨가 좋아서 걸어서 학교에 갔다.";

		OktAnnotator ann = new OktAnnotator();
		ann.setProperty("target", "text");

		Document doc = (new DocumentBuilder()).text(text).build();

		System.err.println(DocumentUtil.toPrettyJsonString(doc));

		ann.annotate(doc);

		System.err.println(DocumentUtil.toPrettyJsonString(doc));

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
		}

	}
	public void testAnnotateDocument002() throws Exception {

		// 韓国に旅行で行った。
		String text = "한국으로 여행을 다녀왔다.";

		OktAnnotator ann = new OktAnnotator();
		ann.setProperty("target", "text");

		Document doc = (new DocumentBuilder()).text(text).build();

		System.err.println(DocumentUtil.toPrettyJsonString(doc));

		ann.annotate(doc);

		System.err.println(DocumentUtil.toPrettyJsonString(doc));

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
		}

	}
	public void testAnnotateDocument003() throws Exception {

		// この花は美しい
		String text = "이 꽃은 아름답다";

		OktAnnotator ann = new OktAnnotator();
		ann.setProperty("target", "text");

		Document doc = (new DocumentBuilder()).text(text).build();

		System.err.println(DocumentUtil.toPrettyJsonString(doc));

		ann.annotate(doc);

		System.err.println(DocumentUtil.toPrettyJsonString(doc));

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
		}

	}
	public void testAnnotateDocument004() throws Exception {

		// ツイッターが開発した韓国語形態素解析ライブラリです
		String text = "트위터가 개발한 한국어 형태소 분석 라이브러리입니다.";

		OktAnnotator ann = new OktAnnotator();
		ann.setProperty("target", "text");

		Document doc = (new DocumentBuilder()).text(text).build();

		System.err.println(DocumentUtil.toPrettyJsonString(doc));

		ann.annotate(doc);

		System.err.println(DocumentUtil.toPrettyJsonString(doc));

		for (Keyword kwd : doc.getKeywords()) {
			System.err.println(kwd);
		}

	}
}

package nlp4j;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.impl.DefaultDocument;
import nlp4j.indexer.DocumentIndexer;
import nlp4j.indexer.SimpleDocumentIndex;
import nlp4j.yhoo_jp.YJpMaAnnotator;

public class IntegrationTestCase extends TestCase {

	public void test001() throws Exception {

		List<Document> docs = new ArrayList<Document>();
		{
			docs.add(createDocument("Toyota", "ハイブリッドカーを作っています。"));
			docs.add(createDocument("Toyota", "ハイブリッドカーを売っています。"));
			docs.add(createDocument("Toyota", "自動車を作っています。"));
			docs.add(createDocument("Toyota", "自動車を売っています。"));
			docs.add(createDocument("Nissan", "EVを作っています。"));
			docs.add(createDocument("Nissan", "EVを売っています。"));
			docs.add(createDocument("Nissan", "自動車を売っています。"));
			docs.add(createDocument("Nissan", "ルノーと提携しています。"));
			docs.add(createDocument("Nissan", "軽自動車を売っています。"));
			docs.add(createDocument("Honda", "自動車を作っています。"));
			docs.add(createDocument("Honda", "自動車を売っています。"));
			docs.add(createDocument("Honda", "バイクを作っています。"));
			docs.add(createDocument("Honda", "バイクを売っています。"));
			docs.add(createDocument("Honda", "軽自動車を売っています。"));
			docs.add(createDocument("Honda", "軽自動車を作っています。"));
		}

		DocumentAnnotator annotator = new YJpMaAnnotator();
		{
			// 形態素解析処理
			annotator.annotate(docs);
		}

		DocumentIndexer index = new SimpleDocumentIndex();
		{
			// キーワードインデックス作成処理
			index.addDocuments(docs);
		}

		{
			// 共起性の高いキーワードの取得
			List<Keyword> kwds = index.getKeywords("名詞", "item=Nissan");

			System.out.println("Keywords(名詞) for Nissan");
			for (Keyword kwd : kwds) {
				System.out.println(String.format("%.1f,%s", kwd.getCorrelation(), kwd.getLex()));
			}
		}
		{
			// 共起性の高いキーワードの取得
			List<Keyword> kwds = index.getKeywords("名詞", "item=Toyota");

			System.out.println("Keywords(名詞) for Toyota");
			for (Keyword kwd : kwds) {
				System.out.println(String.format("%.1f,%s", kwd.getCorrelation(), kwd.getLex()));
			}
		}
		{
			// 共起性の高いキーワードの取得
			List<Keyword> kwds = index.getKeywords("名詞", "item=Honda");

			System.out.println("Keywords(名詞) for Honda");
			for (Keyword kwd : kwds) {
				System.out.println(String.format("%.1f,%s", kwd.getCorrelation(), kwd.getLex()));
			}
		}

	}

	public void test501() throws Exception {

		List<Document> docs = new ArrayList<Document>();
		{
			docs.add(createDocument("Toyota", ""));
			docs.add(createDocument("Nissan", ""));
			docs.add(createDocument("Honda", ""));
		}

		DocumentAnnotator annotator = new YJpMaAnnotator();
		{
			// 形態素解析処理
			annotator.annotate(docs);
		}

		DocumentIndexer index = new SimpleDocumentIndex();
		{
			// キーワードインデックス作成処理
			index.addDocuments(docs);
		}

		{
			// 共起性の高いキーワードの取得
			List<Keyword> kwds = index.getKeywords("名詞", "item=Nissan");

			System.out.println("Keywords(名詞) for Nissan");
			for (Keyword kwd : kwds) {
				System.out.println(String.format("%.1f,%s", kwd.getCorrelation(), kwd.getLex()));
			}
		}
		{
			// 共起性の高いキーワードの取得
			List<Keyword> kwds = index.getKeywords("名詞", "item=Toyota");

			System.out.println("Keywords(名詞) for Toyota");
			for (Keyword kwd : kwds) {
				System.out.println(String.format("%.1f,%s", kwd.getCorrelation(), kwd.getLex()));
			}
		}
		{
			// 共起性の高いキーワードの取得
			List<Keyword> kwds = index.getKeywords("名詞", "item=Honda");

			System.out.println("Keywords(名詞) for Honda");
			for (Keyword kwd : kwds) {
				System.out.println(String.format("%.1f,%s", kwd.getCorrelation(), kwd.getLex()));
			}
		}

	}

	public void test502() throws Exception {

		List<Document> docs = new ArrayList<Document>();
		{
//			docs.add(createDocument("Toyota", ""));
//			docs.add(createDocument("Nissan", ""));
//			docs.add(createDocument("Honda", ""));
		}

		DocumentAnnotator annotator = new YJpMaAnnotator();
		{
			// 形態素解析処理
			annotator.annotate(docs);
		}

		DocumentIndexer index = new SimpleDocumentIndex();
		{
			// キーワードインデックス作成処理
			index.addDocuments(docs);
		}

		{
			// 共起性の高いキーワードの取得
			List<Keyword> kwds = index.getKeywords("名詞", "item=Nissan");

			System.out.println("Keywords(名詞) for Nissan");
			for (Keyword kwd : kwds) {
				System.out.println(String.format("%.1f,%s", kwd.getCorrelation(), kwd.getLex()));
			}
		}
		{
			// 共起性の高いキーワードの取得
			List<Keyword> kwds = index.getKeywords("名詞", "item=Toyota");

			System.out.println("Keywords(名詞) for Toyota");
			for (Keyword kwd : kwds) {
				System.out.println(String.format("%.1f,%s", kwd.getCorrelation(), kwd.getLex()));
			}
		}
		{
			// 共起性の高いキーワードの取得
			List<Keyword> kwds = index.getKeywords("名詞", "item=Honda");

			System.out.println("Keywords(名詞) for Honda");
			for (Keyword kwd : kwds) {
				System.out.println(String.format("%.1f,%s", kwd.getCorrelation(), kwd.getLex()));
			}
		}

	}

	static Document createDocument(String item, String text) {
		Document doc = new DefaultDocument();
		doc.putAttribute("item", item);
		doc.setText(text);
		return doc;
	}

}

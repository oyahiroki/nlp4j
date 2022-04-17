package example;

import java.util.ArrayList;
import java.util.List;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentAnnotatorPipeline;
import nlp4j.DocumentImporter;
import nlp4j.Keyword;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultDocumentAnnotator;
import nlp4j.impl.DefaultDocumentAnnotatorPipeline;
import nlp4j.impl.DefaultDocumentImporter;
import nlp4j.yhoo_jp.YJpMaAnnotator;

/**
 * アノテーション機能を利用するサンプルです。<br>
 * Example for annotation functions.
 * 
 * @author Hiroki Oya
 *
 */
public class DocumentProcessExample1Main {

	/**
	 * アノテーション機能を利用するサンプルです。<br>
	 * Example for annotation functions.
	 * @param args 無し
	 * @throws Exception 実行時の例外 
	 */
	public static void main(String[] args) throws Exception {

		// ドキュメントの用意
		List<Document> docs = new ArrayList<>();
		{
			Document doc = new DefaultDocument();
			doc.setText("これはテストです。");
			docs.add(doc);
		}
		// ドキュメントの表示（キーワードは無し）
		System.err.println(docs);

		// NLPパイプライン（複数の処理をパイプラインとして連結することで処理する）の定義
		DocumentAnnotatorPipeline pipeline = new DefaultDocumentAnnotatorPipeline();
		{
			// サンプルアノテーター
			DocumentAnnotator annotator = new DefaultDocumentAnnotator();
			pipeline.add(annotator);
		}
		{
			// Yahoo! Japan の形態素解析APIを利用するアノテーター
			DocumentAnnotator annotator = new YJpMaAnnotator();
			pipeline.add(annotator);
		}
		// アノテーション処理の実行
		pipeline.annotate(docs);
		// 処理後のドキュメントを表示
		System.err.println(docs);

		// 各文書について
		for (Document doc : docs) {
			// キーワードの一覧を取得
			for (Keyword kwd : doc.getKeywords()) {
				System.err.println("facet=" + kwd.getFacet() + ",lex=" + kwd.getLex());
			}
		}

		// ドキュメントインポーター
		// 現時点では実装を用意していないが、Apache SolrやAzure Searchにインポートすることで分析処理が容易になる
		DocumentImporter importer = new DefaultDocumentImporter();
		{
			importer.importDocuments(docs);
		}

	}

}


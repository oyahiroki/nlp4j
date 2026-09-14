package nlp4j.krmj.annotator;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.impl.DefaultDocument;

/**
 * KuromojiAnnotator 処理パフォーマンス計測テスト
 *
 * <p>以下の2つのシナリオを計測する：
 * <ol>
 *   <li>毎回 new KuromojiAnnotator() するケース（Tokenizer も毎回初期化）</li>
 *   <li>KuromojiAnnotator インスタンスを再利用するケース（Tokenizer を1度だけ初期化）</li>
 * </ol>
 * 各シナリオは独立して時間計測し、最後に比較サマリーを出力する。
 *
 * @author Hiroki Oya
 * @since 1.2
 */
public class KuromojiAnnotatorPerformanceTestCase extends TestCase {

	/** 計測に使用するサンプルテキスト */
	private static final String[] SAMPLE_TEXTS = {
		"私は学校に行きました。",
		"犬が急いで走っている。",
		"日本語の自然言語処理はとても興味深い分野です。",
		"東京都千代田区のオフィスで会議が開かれました。",
		"機械学習とディープラーニングは人工知能の重要な技術です。",
		"私はEVを買いました。",
		"春の桜が満開になり、公園には多くの人が訪れた。",
		"新幹線に乗って大阪から東京まで移動した。",
		"彼女は毎朝コーヒーを飲みながら新聞を読む習慣がある。",
		"このプログラムは形態素解析を使って日本語テキストを処理します。",
	};

	/** 繰り返し回数 */
	private static final int REPEAT = 100;

	/** シナリオ1 の計測結果（ms）を保持するクラス変数 */
	private static long elapsedScenario1 = -1;

	/** シナリオ2 の計測結果（ms）を保持するクラス変数 */
	private static long elapsedScenario2 = -1;

	/**
	 * シナリオ1：毎回 new KuromojiAnnotator() するケース
	 * （annotate() のたびに Tokenizer が new される現状の実装）
	 */
	public void testScenario1_NewInstanceEveryTime() throws Exception {

		System.out.println("========================================");
		System.out.println("シナリオ1: 毎回 new KuromojiAnnotator()");
		System.out.printf("  テキスト数: %d  繰り返し: %d  総処理数: %d%n",
				SAMPLE_TEXTS.length, REPEAT, SAMPLE_TEXTS.length * REPEAT);

		// ウォームアップ（JIT を安定させる）
		for (int i = 0; i < 3; i++) {
			KuromojiAnnotator ann = new KuromojiAnnotator();
			ann.setProperty("target", "text");
			Document doc = new DefaultDocument();
			doc.putAttribute("text", SAMPLE_TEXTS[0]);
			ann.annotate(doc);
		}

		long start = System.currentTimeMillis();
		int totalKeywords = 0;

		for (int i = 0; i < REPEAT; i++) {
			for (String text : SAMPLE_TEXTS) {
				// 毎イテレーションでインスタンスを生成（= Tokenizer も毎回 new）
				KuromojiAnnotator ann = new KuromojiAnnotator();
				ann.setProperty("target", "text");
				Document doc = new DefaultDocument();
				doc.putAttribute("text", text);
				ann.annotate(doc);
				totalKeywords += doc.getKeywords().size();
			}
		}

		elapsedScenario1 = System.currentTimeMillis() - start;

		System.out.printf("  処理時間      : %d ms%n", elapsedScenario1);
		System.out.printf("  総キーワード数: %d%n", totalKeywords);
		System.out.printf("  1処理あたり   : %.2f ms%n",
				(double) elapsedScenario1 / (SAMPLE_TEXTS.length * REPEAT));
		System.out.println("========================================");

		assertTrue("処理が完了すること", totalKeywords > 0);
	}

	/**
	 * シナリオ2：KuromojiAnnotator インスタンスを再利用するケース
	 * （Tokenizer が一度だけ初期化される改善案）
	 */
	public void testScenario2_ReuseInstance() throws Exception {

		System.out.println("========================================");
		System.out.println("シナリオ2: KuromojiAnnotator インスタンスを再利用");
		System.out.printf("  テキスト数: %d  繰り返し: %d  総処理数: %d%n",
				SAMPLE_TEXTS.length, REPEAT, SAMPLE_TEXTS.length * REPEAT);

		// インスタンスを一度だけ生成（= Tokenizer の初期化も1回）
		KuromojiAnnotator ann = new KuromojiAnnotator();
		ann.setProperty("target", "text");

		// ウォームアップ
		for (int i = 0; i < 3; i++) {
			Document doc = new DefaultDocument();
			doc.putAttribute("text", SAMPLE_TEXTS[0]);
			ann.annotate(doc);
		}

		long start = System.currentTimeMillis();
		int totalKeywords = 0;

		for (int i = 0; i < REPEAT; i++) {
			for (String text : SAMPLE_TEXTS) {
				Document doc = new DefaultDocument();
				doc.putAttribute("text", text);
				ann.annotate(doc);
				totalKeywords += doc.getKeywords().size();
			}
		}

		elapsedScenario2 = System.currentTimeMillis() - start;

		System.out.printf("  処理時間      : %d ms%n", elapsedScenario2);
		System.out.printf("  総キーワード数: %d%n", totalKeywords);
		System.out.printf("  1処理あたり   : %.2f ms%n",
				(double) elapsedScenario2 / (SAMPLE_TEXTS.length * REPEAT));
		System.out.println("========================================");

		// 比較サマリー（両シナリオとも実行済みの場合のみ出力）
		if (elapsedScenario1 >= 0 && elapsedScenario2 >= 0) {
			System.out.println();
			System.out.println("========== パフォーマンス比較サマリー ==========");
			System.out.printf("  シナリオ1 (毎回 new)  : %6d ms%n", elapsedScenario1);
			System.out.printf("  シナリオ2 (再利用)    : %6d ms%n", elapsedScenario2);
			long diff = elapsedScenario1 - elapsedScenario2;
			double ratio = elapsedScenario1 > 0
					? (double) elapsedScenario2 / elapsedScenario1 * 100 : 100;
			System.out.printf("  差分                  : %+d ms%n", -diff);
			System.out.printf("  再利用は毎回 new の   : %.1f%%%n", ratio);
			System.out.println("===============================================");
		}

		assertTrue("処理が完了すること", totalKeywords > 0);
	}

}

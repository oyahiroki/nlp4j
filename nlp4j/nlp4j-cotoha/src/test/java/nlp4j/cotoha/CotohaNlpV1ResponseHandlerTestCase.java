package nlp4j.cotoha;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.impl.DefaultKeywordWithDependency;

/**
 * COTOHA APIの構文解析結果をパースするテスト
 * 
 * @author Hiroki Oya
 *
 */
public class CotohaNlpV1ResponseHandlerTestCase extends TestCase {

	private void printResult(CotohaNlpV1ResponseHandler handler) {
		for (DefaultKeywordWithDependency root : handler.getRoots()) {
			System.err.println(root.toStringAsDependencyTree());
		}

		System.err.println("---");

		for (Keyword kwd : handler.getKeywords()) {
			System.err.println(kwd.getLex() + " (" + "word." + kwd.getFacet() + ")");
			System.err.println("\t" + kwd);
		}

		System.err.println("---");

		for (Keyword kwd : handler.getPatternKeywords()) {
			System.err.println(kwd.getLex() + " (" + "pattern." + kwd.getFacet() + ")");
			System.err.println("\t" + kwd);
		}

		System.err.println("---");
		for (Keyword kwd : handler.getChunkLinkKeywords()) {
			System.err.println(kwd.getLex() + " (" + "pattern." + kwd.getFacet() + ")");
			System.err.println("\t" + kwd);
		}

	}

	/**
	 * 「今日はいい天気です。」の解析結果をパースするテスト
	 * 
	 * @throws Exception 例外発生時
	 */
	public void testParse001() throws Exception {

		File file = new File("src/test/resources/nlp_v1_parse_001.json");
		String json = FileUtils.readFileToString(file, "UTF-8");

		CotohaNlpV1ResponseHandler handler = new CotohaNlpV1ResponseHandler();
		handler.parse(json);

		List<DefaultKeywordWithDependency> roots = handler.getRoots();

		for (DefaultKeywordWithDependency root : roots) {
			System.err.println(root.toStringAsDependencyTree());
		}

		System.err.println("---");

		for (Keyword kwd : handler.getKeywords()) {
			System.err.println(kwd.toString());
		}

	}

	/**
	 * 「今日はいい天気です。明日は学校に行きます。」の解析結果をパースするテスト
	 * 
	 * @throws Exception 例外発生時
	 */
	public void testParse002() throws Exception {

		File file = new File("src/test/resources/nlp_v1_parse_002.json");
		String json = FileUtils.readFileToString(file, "UTF-8");

		CotohaNlpV1ResponseHandler handler = new CotohaNlpV1ResponseHandler();
		handler.parse(json);

		printResult(handler);
	}

	/**
	 * 「高速道路を走行中、エンジンが煙を出して急に停止した。」の解析結果をパースするテスト
	 * 
	 * @throws Exception 例外発生時
	 */
	public void testParse003() throws Exception {

		File file = new File("src/test/resources/nlp_v1_parse_003.json");
		String json = FileUtils.readFileToString(file, "UTF-8");

		CotohaNlpV1ResponseHandler handler = new CotohaNlpV1ResponseHandler();
		handler.parse(json);

		printResult(handler);

	}

	/**
	 * 「私と息子は焼き肉を食べた。」の解析結果をパースするテスト
	 * 
	 * @throws Exception 例外発生時
	 */
	public void testParse004() throws Exception {

		File file = new File("src/test/resources/nlp_v1_parse_004.json");
		String json = FileUtils.readFileToString(file, "UTF-8");

		CotohaNlpV1ResponseHandler handler = new CotohaNlpV1ResponseHandler();
		handler.parse(json);

		printResult(handler);
	}

	/**
	 * 「嫁と娘は旅行に行った。私と息子は焼き肉を食べた。」の解析結果をパースするテスト
	 * 
	 * @throws Exception 例外発生時
	 */
	public void testParse005() throws Exception {

		File file = new File("src/test/resources/nlp_v1_parse_005.json");
		String json = FileUtils.readFileToString(file, "UTF-8");

		CotohaNlpV1ResponseHandler handler = new CotohaNlpV1ResponseHandler();
		handler.parse(json);

		printResult(handler);
	}

	/**
	 * 「私と息子は3時に公園で弁当を食べた。」の解析結果をパースするテスト
	 * 
	 * @throws Exception 例外発生時
	 */
	public void testParse006() throws Exception {

		File file = new File("src/test/resources/nlp_v1_parse_006.json");
		String json = FileUtils.readFileToString(file, "UTF-8");

		CotohaNlpV1ResponseHandler handler = new CotohaNlpV1ResponseHandler();
		handler.parse(json);

		printResult(handler);
	}

	/**
	 * 「私は3時に歩いて学校に行きました。」の解析結果をパースするテスト
	 * 
	 * @throws Exception 例外発生時
	 */
	public void testParse007() throws Exception {

		File file = new File("src/test/resources/nlp_v1_parse_007.json");
		String json = FileUtils.readFileToString(file, "UTF-8");

		CotohaNlpV1ResponseHandler handler = new CotohaNlpV1ResponseHandler();
		handler.parse(json);

		printResult(handler);
	}

	/**
	 * 「太郎は花子に手紙を送る」の解析結果をパースするテスト
	 * 
	 * @throws Exception 例外発生時
	 */
	public void testParse008() throws Exception {

		File file = new File("src/test/resources/nlp_v1_parse_008.json");
		String json = FileUtils.readFileToString(file, "UTF-8");

		CotohaNlpV1ResponseHandler handler = new CotohaNlpV1ResponseHandler();
		handler.parse(json);

		printResult(handler);
	}

	/**
	 * Gsonの整形を試す
	 * 
	 * @throws Exception 例外発生時
	 */
	public void testParse008b() throws Exception {

		File file = new File("src/test/resources/nlp_v1_parse_008.json");
		String json = FileUtils.readFileToString(file, "UTF-8");

		Gson gson = new Gson();

		JsonObject jsonObj = gson.fromJson(json, JsonObject.class);

		System.err.println(jsonObj);

	}

}

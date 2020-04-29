package nlp4j.cotoha;

import junit.framework.TestCase;
import nlp4j.impl.DefaultNlpServiceResponse;

public class CotohaNlpServiceTestCase extends TestCase {

	Class target = CotohaNlpService.class;

	public void testCotohaNlpService() {
		fail("Not yet implemented");
	}

	public void testAccessToken() throws Exception {

		CotohaNlpService service = new CotohaNlpService();

		AccessToken accessToken = service.accessToken();

		System.err.println(accessToken);

		assertNotNull(accessToken);

		assertNotNull(accessToken.getAccess_token());
	}

	public void testNlpV1ParseString001() throws Exception {
		CotohaNlpService service = new CotohaNlpService();
		DefaultNlpServiceResponse response = service.nlpV1Parse("今日は学校に走って行きました。");
		System.err.println(response.getOriginalResponseBody());
	}

	public void testNlpV1ParseString002() throws Exception {
		CotohaNlpService service = new CotohaNlpService();
		DefaultNlpServiceResponse response = service.nlpV1Parse("今日はいい天気です。明日は学校に行きます。");
		System.err.println(response.getOriginalResponseBody());
	}

	public void testNlpV1ParseString003() throws Exception {
		CotohaNlpService service = new CotohaNlpService();
		DefaultNlpServiceResponse response = service.nlpV1Parse("高速道路を走行中、エンジンが煙を出して急に停止した。");
		System.err.println(response.getOriginalResponseBody());
	}

	public void testNlpV1ParseString004() throws Exception {
		CotohaNlpService service = new CotohaNlpService();
		DefaultNlpServiceResponse response = service.nlpV1Parse("私と息子は焼き肉を食べた。");
		System.err.println(response.getOriginalResponseBody());
	}

	public void testNlpV1ParseString005() throws Exception {
		CotohaNlpService service = new CotohaNlpService();
		DefaultNlpServiceResponse response = service.nlpV1Parse("嫁と娘は旅行に行った。私と息子は焼き肉を食べた。");
		System.err.println(response.getOriginalResponseBody());
	}

	public void testNlpV1ParseString005a() throws Exception {
		CotohaNlpService service = new CotohaNlpService();
		DefaultNlpServiceResponse response = service.nlpV1Parse("嫁と娘は旅行に行った。");
		System.err.println(response.getOriginalResponseBody());
	}

	public void testNlpV1ParseString005b() throws Exception {
		CotohaNlpService service = new CotohaNlpService();
		DefaultNlpServiceResponse response = service.nlpV1Parse("私と息子は焼き肉を食べた。");
		System.err.println(response.getOriginalResponseBody());
	}

	public void testNlpV1ParseString006() throws Exception {
		CotohaNlpService service = new CotohaNlpService();
		DefaultNlpServiceResponse response = service.nlpV1Parse("私と息子は３時に公園で弁当を食べた。");
		System.err.println(response.getOriginalResponseBody());
	}

	public void testNlpV1ParseString007() throws Exception {
		CotohaNlpService service = new CotohaNlpService();
		DefaultNlpServiceResponse response = service.nlpV1Parse("私は３時に歩いて学校に行きました。");
		System.err.println(response.getOriginalResponseBody());
	}

	public void testNlpV1ParseString008() throws Exception {
		CotohaNlpService service = new CotohaNlpService();
		DefaultNlpServiceResponse response = service.nlpV1Parse("太郎は花子に手紙を送った。");
		System.err.println(response.getOriginalResponseBody());
	}

	public void testNlpV1ParseStringString() {
		fail("Not yet implemented");
	}

}

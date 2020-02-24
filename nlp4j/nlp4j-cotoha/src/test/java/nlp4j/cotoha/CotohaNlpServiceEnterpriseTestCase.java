package nlp4j.cotoha;

import junit.framework.TestCase;
import nlp4j.impl.DefaultNlpServiceResponse;

public class CotohaNlpServiceEnterpriseTestCase extends TestCase {

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
		
//		COTOHA_CLIENT_ID
//		COTOHA_CLIENT_SECRET
//		COTOHA_URL_ACCESSTOKEN
//		COTOHA_API_BASE_URL

		CotohaNlpService service = new CotohaNlpService();
		DefaultNlpServiceResponse response = service.nlpV1Parse("今日は学校に走って行きました。");
		System.err.println(response.getOriginalResponseBody());
	}

}

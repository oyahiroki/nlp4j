package nlp4j.cotoha;

import java.io.File;

import nlp4j.impl.DefaultNlpServiceResponse;
import nlp4j.util.JsonUtils;

public class CotohaNlpServiceTestMain001 {

	public static void main(String[] args) throws Exception {

		File out = new File("src/test/resources/nlp4j.cotoha/nlp_v1_parse_out_001.txt");

		CotohaNlpService service = new CotohaNlpService();
		DefaultNlpServiceResponse response = service.nlpV1Parse("今日は学校に走って行きました。");
//		System.err.println(response.getOriginalResponseBody());

		JsonUtils.write(out, response.getOriginalResponseBody());

	}

}

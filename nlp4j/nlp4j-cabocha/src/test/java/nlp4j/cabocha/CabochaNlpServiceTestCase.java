package nlp4j.cabocha;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultNlpServiceResponse;

public class CabochaNlpServiceTestCase extends TestCase {

	public void testProcess001() throws Exception {
		String text = "今日はいい天気です。";
		CabochaNlpService service = new CabochaNlpService();
		service.setProperty("tempDir", System.getProperty("java.io.tmpdir"));
		service.setProperty("encoding", "MS932");
		DefaultNlpServiceResponse response = service.process(text);
		for (Keyword kwd : response.getKeywords()) {
			if (kwd instanceof KeywordWithDependency) {
				System.err.println(((KeywordWithDependency) kwd).toStringAsXml());
			}
		}
	}

	public void testProcess002() throws Exception {
		String text = "私は同志社大学に歩いて行きました。";
		CabochaNlpService service = new CabochaNlpService();
		service.setProperty("tempDir", System.getProperty("java.io.tmpdir"));
		service.setProperty("encoding", "MS932");
		DefaultNlpServiceResponse response = service.process(text);
		for (Keyword kwd : response.getKeywords()) {
			if (kwd instanceof KeywordWithDependency) {
				System.err.println(((KeywordWithDependency) kwd).toStringAsXml());
			}
		}
	}

}

package nlp4j.cabocha;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultKeywordWithDependency;
import nlp4j.impl.DefaultNlpServiceResponse;
import nlp4j.util.KeywordUtil;

@SuppressWarnings("javadoc")
public class CabochaNlpServiceTestCase extends TestCase {

	public void testProcess001() throws Exception {
		String text = "今日はいい天気です。";
		CabochaNlpService service = new CabochaNlpService();
		service.setProperty("tempDir", "R:");
		service.setProperty("encoding", "MS932");
		DefaultNlpServiceResponse response = service.process(text);
		for (Keyword kwd : response.getKeywords()) {
			if (kwd instanceof KeywordWithDependency) {
				System.err.println(((KeywordWithDependency) kwd).getSequence());
				System.err.println(((KeywordWithDependency) kwd).toStringAsXml());

				DefaultKeywordWithDependency kw = (DefaultKeywordWithDependency) kwd;
				assertKeyword(kw);

				List<Keyword> kk = KeywordUtil.toKeywordList(kw);
				for (Keyword k : kk) {
					System.err.println(k);
				}

			}
		}
	}

	private void assertKeyword(KeywordWithDependency kw) {
		System.err
				.println(kw.getSequence() + ",begin=" + kw.getBegin() + ",end=" + kw.getEnd() + ",lex=" + kw.getLex());

		for (KeywordWithDependency k : kw.getChildren()) {
			assertKeyword(k);
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

package nlp4j.ginza;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.NlpServiceResponse;

/**
 * @author Hiroki Oya
 * @created_at 2021-04-24
 */
public class GinzaNlpServiceViaProcessTestCase extends TestCase {

	public void testProcess001() throws Exception {

		GinzaNlpServiceViaProcess nlp = new GinzaNlpServiceViaProcess();

		nlp.setProperty("tempDir", "C:/temp");
		nlp.setProperty("encoding", "MS932");

		NlpServiceResponse response = nlp.process("今日はいい天気です。");

		List<Keyword> kwds = response.getKeywords();

		assertTrue(kwds.size() > 0);

		for (Keyword kwd : kwds) {
			if (kwd instanceof KeywordWithDependency) {
				System.err.println(((KeywordWithDependency) kwd).toStringAsXml());
			}
		}

	}

	public void testProcess002() throws Exception {

		GinzaNlpServiceViaProcess nlp = new GinzaNlpServiceViaProcess();

		nlp.setProperty("tempDir", "R:");
		nlp.setProperty("encoding", "MS932");

		NlpServiceResponse response = nlp.process("私は本屋で本を買いました。");

		List<Keyword> kwds = response.getKeywords();

		assertTrue(kwds.size() > 0);

		for (Keyword kwd : kwds) {
			if (kwd instanceof KeywordWithDependency) {
				System.err.println(((KeywordWithDependency) kwd).toStringAsXml());
			}
		}

	}

	public void testProcess101() throws Exception {

		GinzaNlpServiceViaProcess nlp = new GinzaNlpServiceViaProcess();

		File tempDir = new File("R:");
		if (tempDir.exists() && tempDir.isDirectory() && tempDir.canWrite()) {
			System.err.println("Set temp Dir: " + tempDir.getAbsolutePath());
			nlp.setProperty("tempDir", tempDir.getAbsolutePath());
		}

		nlp.setProperty("encoding", "MS932");

		NlpServiceResponse response = nlp.process("今日はいい天気です。");

		List<Keyword> kwds = response.getKeywords();

		assertTrue(kwds.size() > 0);

		for (Keyword kwd : kwds) {
			if (kwd instanceof KeywordWithDependency) {
				System.err.println(((KeywordWithDependency) kwd).toStringAsXml());
			}
		}

	}

	public void testProcess102() throws Exception {

		GinzaNlpServiceViaProcess nlp = new GinzaNlpServiceViaProcess();

		File tempDir = new File("R:");
		if (tempDir.exists() && tempDir.isDirectory() && tempDir.canWrite()) {
			System.err.println("Set temp Dir: " + tempDir.getAbsolutePath());
			nlp.setProperty("tempDir", tempDir.getAbsolutePath());
		}

		nlp.setProperty("encoding", "MS932");

		ArrayList<Long> logTime = new ArrayList<>();

		for (int n = 0; n < 10; n++) {
			long time1 = System.currentTimeMillis();
			NlpServiceResponse response = nlp.process("今日はいい天気です。");

			List<Keyword> kwds = response.getKeywords();

			assertTrue(kwds.size() > 0);

			for (Keyword kwd : kwds) {
				if (kwd instanceof KeywordWithDependency) {
					System.err.println(((KeywordWithDependency) kwd).toStringAsXml());
				}
			}
			long time2 = System.currentTimeMillis();

			System.err.println("time: " + (time2 - time1));
			logTime.add((time2 - time1));
		}

		System.err.println(logTime);

	}

}

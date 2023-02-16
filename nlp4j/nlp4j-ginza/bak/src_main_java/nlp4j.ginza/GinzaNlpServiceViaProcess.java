package nlp4j.ginza;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.NlpService;
import nlp4j.impl.DefaultKeywordWithDependency;
import nlp4j.impl.DefaultNlpServiceResponse;

/**
 * @author Hiroki Oya
 * @since 0.1.0.0
 * @created_at 2021-04-18
 */
public class GinzaNlpServiceViaProcess implements NlpService {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	Properties props = new Properties();

	private String encoding = "MS932";
	private String tempDir = System.getProperty("java.io.tmpdir");

	/**
	 * @param key   "tempDir" | "encoding"
	 * @param value Value of property
	 */
	public void setProperty(String key, String value) {
		this.props.setProperty(key, value);
		if ("tempDir".equals(key)) {

			{
				File dir = new File(value);
				if (dir.exists() == false || dir.isDirectory() == false || dir.canRead() == false
						|| dir.canWrite() == false) {
					throw new RuntimeException("Invalid value for tempDir: " + value);
				}
			}

			this.tempDir = value;
		} //
		else if ("encoding".equals(key)) {
			this.encoding = value;
		}
	}

	/**
	 * @param text Text in Natural Language
	 * @return NLP Result by Ginza
	 * @throws IOException on ERROR
	 */
	public DefaultNlpServiceResponse process(String text) throws IOException {

		File dir = new File(tempDir);

		File file1 = File.createTempFile("nlp", ".txt", dir);

		logger.debug("TempFile: " + file1.getAbsolutePath());

		FileUtils.write(file1, text, encoding);

		List<String> command = new ArrayList<>();

		command.add("ginza");
		command.add(file1.getAbsolutePath());

		ProcessBuilder pb = new ProcessBuilder(command);

//		pb.redirectErrorStream(true);
//		pb.redirectOutput(log.toFile());

		try {

			long time1 = System.currentTimeMillis();

			Process process = pb.start(); // ctart process

//			System.err.println(pb.redirectInput().toString());

			InputStream is = process.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, encoding));

			int exitCode = -1;
//
//			while (true) {
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//				}
//				if (process.isAlive() == false) {
//					exitCode = process.exitValue();
//					break;
//				}
//			}

			exitCode = process.waitFor();

			long time2 = System.currentTimeMillis();

			logger.debug("time: " + (time2 - time1));

			logger.debug("ProcessExitCode: " + exitCode);

			HashMap<String, DefaultKeywordWithDependency> kwdMap = new HashMap<>();

			StringBuilder originalResponse = new StringBuilder();

			try {
				String line;
				while ((line = br.readLine()) != null) {
					logger.debug("line: " + line);
					originalResponse.append(line + "\n");

					if (line.startsWith("#")) {
						continue;
					}
					if (line.contains("\t") == false) {
						continue;
					}

					extractKeyword(kwdMap, line);

				} // END OF WHILE

			} finally {
				br.close();
			}

			KeywordWithDependency root = null;

			for (DefaultKeywordWithDependency kwd : kwdMap.values()) {
				String k = kwd.getDependencyKey();
				if (kwdMap.containsKey(k)) {
					kwd.setParent(kwdMap.get(k));
					root = kwd.getRoot();
				}
			}

			DefaultNlpServiceResponse response = new DefaultNlpServiceResponse();
			ArrayList<Keyword> kwds = new ArrayList<>();
			kwds.add(root);
			response.setKeywords(kwds);
			response.setOriginalResponseBody(originalResponse.toString());

			if (root != null) {
				logger.debug(root.toStringAsXml());
			}

			return response;

		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e);
		}

	}

	private void extractKeyword(HashMap<String, DefaultKeywordWithDependency> kwdMap, String line) {
		{
			DefaultKeywordWithDependency kwd = new DefaultKeywordWithDependency();
			String[] ss = line.split("\t");
			String id = ss[0];
			String form = ss[1];
			String lemma = ss[2];
			String upos = ss[3];
			String xpos = ss[4];
			String feats = ss[5];
			String head = ss[6];

			kwd.setStr(form);
			kwd.setLex(lemma);
			kwd.setFacet(upos);
			kwd.setDependencyKey(head);

			kwdMap.put(id, kwd);

			logger.debug("" + lemma + " -> " + head);
		}
	}
}

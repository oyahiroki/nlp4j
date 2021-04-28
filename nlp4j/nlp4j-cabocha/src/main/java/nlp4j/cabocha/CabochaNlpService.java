package nlp4j.cabocha;

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
 * 
 * @author Hiroki Oya
 * @since 0.1.0.0
 * @created_at 21-APR-2021
 *
 */
public class CabochaNlpService implements NlpService {

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

	public DefaultNlpServiceResponse process(String text) throws IOException {
//		String s = "私は走って学校に行きます。";
//		String encoding = "MS932";

		File dir = new File(tempDir);
		File file1 = File.createTempFile("nlp", ".txt", dir);
		file1.deleteOnExit();
		logger.debug("TempFile: " + file1.getAbsolutePath());
		FileUtils.write(file1, text, encoding);

		List<String> command = new ArrayList<>();
		{
			command.add("cabocha");
			command.add("-f1");
			command.add(file1.getAbsolutePath());
		}

		ProcessBuilder pb = new ProcessBuilder(command); // コマンド実行用のプロセスを準備する。
//		pb.redirectErrorStream(true); // 標準出力と標準エラー出力の出力先を同じにする。
//		pb.redirectOutput(log.toFile()); // 標準出力と標準エラー出力の出力先ファイルを設定。

		try {
			Process proc = pb.start(); // コマンドを実行

			InputStream is = proc.getInputStream();

			int exitCode = proc.waitFor(); // コマンドの終了を待機する。

			logger.debug("exitCode=" + exitCode);

			HashMap<String, DefaultKeywordWithDependency> kwdMap = new HashMap<>();

			StringBuilder originalResponse = new StringBuilder();

			BufferedReader br = new BufferedReader(new InputStreamReader(is, encoding));

			String bId = null; // 文節ID
			String bNext = null; // 次の文節のID

			int begin = 0;

			int kwdId = 0;

			DefaultKeywordWithDependency kwdPtr = null;

			try {
				String line = null;
				while ((line = br.readLine()) != null) {

					originalResponse.append(line + "\n");

//					System.err.println(line);

					if (line.equals("EOS")) {
						break;
					} //
					else if (line.startsWith("* ")) {

						if (kwdPtr != null) {
							kwdPtr.setDependencyKey(bNext + "-0");
						}

						kwdId = 0;

						String[] ss = line.split(" ");
						bId = ss[1];
						bNext = ss[2].substring(0, ss[2].length() - 1);

					} //
					else {
						String str = line.substring(0, line.indexOf("\t"));
						String[] ss = line.substring(line.indexOf("\t") + 1).split(",");
						String pos1 = ss[0];
						String pos2 = ss[1];
						String lex = ss[6];
						String reading = ss[7];

						DefaultKeywordWithDependency kwd = new DefaultKeywordWithDependency();
						kwdPtr = kwd;

						String depKey = bId + "-" + (kwdId + 1);
						kwd.setDependencyKey(depKey);

						kwd.setBegin(begin);
						kwd.setEnd(str.length());
						{
							begin += str.length();
						}
						kwd.setFacet(pos1);
						kwd.setLex(lex);
						kwd.setStr(str);

						String kwdKey = bId + "-" + kwdId;
						kwdMap.put(kwdKey, kwd);

						kwdId++;

					}

				} // END OF WHILE
				if (kwdPtr != null) {
					kwdPtr.setDependencyKey(null);
				}
			} finally {
				br.close();
			}

			KeywordWithDependency root = null;
			for (DefaultKeywordWithDependency kwd : kwdMap.values()) {
				logger.debug(kwd.getLex() + " --> " + kwd.getDependencyKey());
				String k = kwd.getDependencyKey();
				if (kwdMap.containsKey(k)) {
					kwd.setParent(kwdMap.get(k));
					root = kwd.getRoot();
				}
			}

			logger.debug(root.toStringAsXml());

//				out.println(getResultMessage(command, exitCode)); // コマンドの実行結果を表示

			DefaultNlpServiceResponse response = new DefaultNlpServiceResponse();
			ArrayList<Keyword> kwds = new ArrayList<>();
			kwds.add(root);
			response.setKeywords(kwds);
			response.setOriginalResponseBody(originalResponse.toString());

			logger.debug(root.toStringAsXml());

			return response;

		} //
		catch (IOException | InterruptedException e) {
			throw new IOException(e);
		}

	} // END OF process()

}

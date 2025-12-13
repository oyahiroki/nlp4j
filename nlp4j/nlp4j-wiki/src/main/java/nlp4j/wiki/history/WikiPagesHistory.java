package nlp4j.wiki.history;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.xml.sax.Attributes;

import nlp4j.counter.Counter;
import nlp4j.util.IOUtils;
import nlp4j.xml.SAXParserUtils;
import nlp4j.xml.StandardXmlHandler;

public class WikiPagesHistory {

	public static void main(String[] args) throws Exception {

		// Exception in thread "main" java.io.IOException:
		// org.xml.sax.SAXParseException; lineNumber: 90918796; columnNumber: 255;
		// JAXP00010004:
		// エンティティの累積サイズ"50,000,001"は、"FEATURE_SECURE_PROCESSING"で設定された制限"50,000,000"を超えました。

		System.setProperty("jdk.xml.totalEntitySizeLimit", "2000000000"); // 2GB
		System.setProperty("jdk.xml.entityExpansionLimit", "0");
		System.setProperty("jdk.xml.maxGeneralEntitySizeLimit", "0");
		System.setProperty("jdk.xml.maxParameterEntitySizeLimit", "0");
		System.setProperty("jdk.xml.entityReplacementLimit", "0");

//		String fileName = "D:\\local\\wiki\\jawiki\\20250901\\jawiki\\20250901\\jawiki-20250901-pages-meta-history2.xml-p390360p390428\\jawiki-20250901-pages-meta-history2.xml-p390360p390428";
//		File f = new File(fileName);

//		File f = new File(
//				"D:\\local\\wiki\\jawiki\\20250901\\jawiki\\20250901\\jawiki-20250901-pages-meta-history2.xml-p390360p390428.7z");

		File dir = new File("D:\\local\\wiki\\jawiki\\20250901\\jawiki\\20250901\\");

//		File f = new File("D:\\local\\wiki\\jawiki\\20250901\\jawiki\\20250901\\"
//				+ "jawiki-20250901-pages-meta-history1.xml-p1p2342.7z");

		Counter<String> counter = new Counter<String>();

		FilenameFilter filenameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith("7z")) {
					return true;
				} else {
					return false;
				}
			}
		};

		int n_files = dir.listFiles(filenameFilter).length;
		int idx = 0;
		for (File f : dir.listFiles(filenameFilter)) {
			idx++;

			System.err.println(f.getAbsolutePath() + " " + idx + "/" + n_files);

			long time1 = System.currentTimeMillis();

			StandardXmlHandler h = new StandardXmlHandler() {
				@Override
				public void startElement(String uri, String localName, String qName, Attributes attributes,
						String path) {
//					System.err.println("start path: " + path);
				}

				@Override
				public void endElement(String uri, String localName, String qName, String path, String text) {
					if ("mediawiki/page/revision/contributor/username".equals(path)) {
						counter.add(text.trim());
					} //
					else if ("mediawiki/page/revision/contributor/ip".equals(path)) {
						counter.add(text.trim());
					}
//					if (path.contains("contributor")) {
//						System.err.println("end path: " + path);
//						System.err.println("text:" + text.trim());
//					} //
//					else if (path.contains("timestamp")) {
//						System.err.println("end path: " + path);
//						System.err.println("text:" + text.trim());
//					}
				}
			};
			try {
				SAXParserUtils.parse(f, h);
			} catch (IOException e) {
				e.printStackTrace();
			}
			long time2 = System.currentTimeMillis();

			System.err.println(f.getAbsolutePath() + " " + ((time2 - time1) / 1000) + " sec");

		}

		// mediawiki/page/revision/contributor/ip

		// mediawiki/page/revision/contributor/username

		File out = File.createTempFile("nlp4j-", ".txt");
		System.err.println(out.getAbsolutePath());

		counter.print(IOUtils.pw(out));
	}

}

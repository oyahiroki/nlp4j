package nlp4j.wiki;

import java.io.File;
import java.io.FileNotFoundException;

import nlp4j.test.NLP4JTestCase;

public class WikiDumpReaderTestCase extends NLP4JTestCase {
	public WikiDumpReaderTestCase() {
		this.target = WikiDumpReader.class;
	}

	public void test001() throws Exception {

		String dumpFileName = "C:/usr/local/data/wiki/" + "jawiktionary-20210401-pages-articles-multistream.xml.bz2";
		File dumpFile = new File(dumpFileName);

		if (dumpFile.exists() == false) {
			FileNotFoundException e = new FileNotFoundException(dumpFile.getAbsolutePath());
			e.printStackTrace();
			// skip this test
			return;
		} //
		else {
			String indexFileName = "src/test/resources/nlp4j.wiki/"
					+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";

			File indexFile = new File(indexFileName);

			WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);

			{
				String itemString = "ヨーロッパ";
				WikiPage page = dumpReader.getItem(itemString);
				System.err.println(page.getPlainText());
			}

			System.err.println("---------------------------------------------");

			{
				String itemString = "大学";
				WikiPage page = dumpReader.getItem(itemString);
				System.err.println(page.getPlainText());
			}

			System.err.println("#####");

			{
				String itemString = "東京";
				WikiPage page = dumpReader.getItem(itemString);
				System.err.println(page.getPlainText());
			}
			System.err.println("#####");

			{
				String itemString = "財布";
				WikiPage page = dumpReader.getItem(itemString);
				System.err.println(page.getPlainText());
			}

			System.err.println("#####");

			{
				String itemString = "椅子";
				WikiPage page = dumpReader.getItem(itemString);
				System.err.println(page.getPlainText());
			}

			// 覆水盆に返らず
			{
				String itemString = "酸素";
				WikiPage page = dumpReader.getItem(itemString);
				System.err.println(page.getPlainText());
			}

			dumpReader.close();

		}

	}

	public void test002() throws Exception {

		String dumpFileName = "C:/usr/local/data/wiki/" + "jawiktionary-20210401-pages-articles-multistream.xml.bz2";
		File dumpFile = new File(dumpFileName);

		if (dumpFile.exists() == false) {
			FileNotFoundException e = new FileNotFoundException(dumpFile.getAbsolutePath());
			e.printStackTrace();
			// skip this test
			return;
		} //
		else {
			String indexFileName = "src/test/resources/nlp4j.wiki/"
					+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";

			File indexFile = new File(indexFileName);

			WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);

			{ // 酸素
				String itemString = "酸素";
				WikiPage page = dumpReader.getItem(itemString);
				System.err.println(page.getPlainText());
			}

			dumpReader.close();

		}

	}

	public void test003() throws Exception {

		String dumpFileName = "C:/usr/local/data/wiki/" + "jawiktionary-20210401-pages-articles-multistream.xml.bz2";
		File dumpFile = new File(dumpFileName);

		if (dumpFile.exists() == false) {
			FileNotFoundException e = new FileNotFoundException(dumpFile.getAbsolutePath());
			e.printStackTrace();
			// skip this test
			return;
		} //
		else {
			String indexFileName = "src/test/resources/nlp4j.wiki/"
					+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";

			File indexFile = new File(indexFileName);

			WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);

			{
				String itemString = "test";
				WikiPage page = dumpReader.getItem(itemString);

//				System.err.println(page.getPlainText());

				System.err.println("---");
				System.err.println(page.getText());
				System.err.println("---");
				System.err.println(page.getText().startsWith("=={{en}}=="));
			}

			dumpReader.close();

		}

	}

	public void test004() throws Exception {
		String dumpFileName = "C:/usr/local/data/wiki/" + "jawiktionary-20210401-pages-articles-multistream.xml.bz2";
		File dumpFile = new File(dumpFileName);
		if (dumpFile.exists() == false) {
			FileNotFoundException e = new FileNotFoundException(dumpFile.getAbsolutePath());
			e.printStackTrace();
			// skip this test
			return;
		} //
		else {
			String indexFileName = "src/test/resources/nlp4j.wiki/"
					+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";
			File indexFile = new File(indexFileName);
			WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);
			{
				String itemString = "テスト";
				WikiPage page = dumpReader.getItem(itemString);
				// System.err.println(page.getPlainText());
				System.err.println("---");
				System.err.println(page.getText());
				System.err.println("---");
				System.err.println(page.getText().startsWith("=={{en}}=="));
			}
			dumpReader.close();
		}
	}

	// 학교
	public void test005() throws Exception {
		String dumpFileName = "C:/usr/local/data/wiki/" + "jawiktionary-20210401-pages-articles-multistream.xml.bz2";
		File dumpFile = new File(dumpFileName);
		if (dumpFile.exists() == false) {
			FileNotFoundException e = new FileNotFoundException(dumpFile.getAbsolutePath());
			e.printStackTrace();
			// skip this test
			return;
		} //
		else {
			String indexFileName = "src/test/resources/nlp4j.wiki/"
					+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";
			File indexFile = new File(indexFileName);
			WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);
			{
				String itemString = "학교";
				WikiPage page = dumpReader.getItem(itemString);
				// System.err.println(page.getPlainText());
				System.err.println("---");
				System.err.println(page.getText());
				System.err.println("---");
				System.err.println(page.getText().startsWith("=={{en}}=="));
			}
			dumpReader.close();
		}
	}

	public void test006() throws Exception {
		String dumpFileName = "C:/usr/local/data/wiki/" + "jawiktionary-20210401-pages-articles-multistream.xml.bz2";
		File dumpFile = new File(dumpFileName);
		if (dumpFile.exists() == false) {
			FileNotFoundException e = new FileNotFoundException(dumpFile.getAbsolutePath());
			e.printStackTrace();
			// skip this test
			return;
		} //
		else {
			String indexFileName = "src/test/resources/nlp4j.wiki/"
					+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";
			File indexFile = new File(indexFileName);
			WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);
			{
				String itemString = "学校";
				WikiPage page = dumpReader.getItem(itemString);
				// System.err.println(page.getPlainText());
				System.err.println("---");
				System.err.println(page.getText());
				System.err.println("---");
				System.err.println(page.getText().startsWith("=={{en}}=="));
				System.err.println("---");
				System.err.println(page.getPlainText());
			}
			dumpReader.close();
		}
	}

	public void test007() throws Exception {
		String dumpFileName = "C:/usr/local/data/wiki/" + "jawiktionary-20210401-pages-articles-multistream.xml.bz2";
		File dumpFile = new File(dumpFileName);
		if (dumpFile.exists() == false) {
			FileNotFoundException e = new FileNotFoundException(dumpFile.getAbsolutePath());
			e.printStackTrace();
			// skip this test
			return;
		} //
		else {
			String indexFileName = "src/test/resources/nlp4j.wiki/"
					+ "jawiktionary-20210401-pages-articles-multistream-index.txt.bz2";
			File indexFile = new File(indexFileName);
			WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile);
			{
				String itemString = "大学";
				WikiPage page = dumpReader.getItem(itemString);
				// System.err.println(page.getPlainText());
				System.err.println("---");
				System.err.println(page.getText());
				System.err.println("---");
				System.err.println(page.getText().startsWith("=={{en}}=="));
				System.err.println("---");
				System.err.println(page.getPlainText());
			}
			dumpReader.close();
		}
	}
}

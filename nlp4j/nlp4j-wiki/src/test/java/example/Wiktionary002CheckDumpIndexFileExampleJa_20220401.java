package example;

import java.io.File;

import nlp4j.wiki.util.MediaWikiFileUtils;

/**
 * <pre>
 * Get File of Index File and Dump File
 * インデックスファイルとダンプファイルの取得
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class Wiktionary002CheckDumpIndexFileExampleJa_20220401 {

	public static void main(String[] args) throws Exception {
		{
			File indexFile = MediaWikiFileUtils.getIndexFile( //
					"/usr/local/data/wiki/20220401", //
					"ja", //
					"wiktionary", //
					"20220401"); //

			System.err.println(indexFile.getAbsolutePath());
			System.err.println(String.format("%,d", indexFile.length()));
		}
		{
			File dumpFile = MediaWikiFileUtils.getDumpFile( //
					"/usr/local/data/wiki/20220401", //
					"ja", //
					"wiktionary", //
					"20220401"); //

			System.err.println(dumpFile.getAbsolutePath());
			System.err.println(String.format("%,d", dumpFile.length()));
		}

	}

}

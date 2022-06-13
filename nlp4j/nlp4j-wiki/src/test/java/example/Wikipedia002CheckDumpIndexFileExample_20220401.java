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
public class Wikipedia002CheckDumpIndexFileExample_20220401 {

	public static void main(String[] args) throws Exception {
		{
			File indexFile = MediaWikiFileUtils.getIndexFile( //
					"/usr/local/data/wiki/20220401", //
					"ja", //
					"wiktionary", //
					"20220401"); //

			System.err.println("path: " + indexFile.getAbsolutePath());
			System.err.println("size: " + String.format("%,d", indexFile.length()));
		}
		{
			File dumpFile = MediaWikiFileUtils.getDumpFile( //
					"/usr/local/data/wiki/20220401", //
					"ja", //
					"wiktionary", //
					"20220401"); //

			System.err.println("path: " + dumpFile.getAbsolutePath());
			System.err.println("size: " + String.format("%,d", dumpFile.length()));
		}

	}

}

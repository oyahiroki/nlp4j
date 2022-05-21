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
public class Wikipedia002CheckDumpIndexFileExample {

	public static void main(String[] args) throws Exception {
		{
			File indexFile = MediaWikiFileUtils.getIndexFile( //
					"/usr/local/data/wiki", //
					"ja", //
					"wiki", //
					"20210401"); //

			System.err.println(indexFile.getAbsolutePath());
			System.err.println(String.format("%,d", indexFile.length()));
		}
		{
			File dumpFile = MediaWikiFileUtils.getDumpFile( //
					"/usr/local/data/wiki", //
					"ja", //
					"wiki", //
					"20210401"); //

			System.err.println(dumpFile.getAbsolutePath());
			System.err.println(String.format("%,d", dumpFile.length()));
		}

	}

}

package nlp4j.wiki.app;

import java.io.File;

import nlp4j.wiki.util.MediaWikiIndexFileUtils;

/**
 * WikiDumpIndexPropertyPrinter - Prints properties of Wikipedia dump index files
 * WikiDumpIndexPropertyPrinter - Wikipediaダンプインデックスファイルのプロパティを出力します
 *
 * This program reads a Wikipedia dump index file (*.txt.bz2) and prints its properties
 * such as file name, size, last update time, and entry count.
 *
 * このプログラムはWikipediaダンプインデックスファイル(*.txt.bz2)を読み込み、
 * ファイル名、サイズ、最終更新日時、エントリ数などのプロパティを出力します。
 *
 * Example output (出力例):
 * <pre>
 * Index file name: C:\usr\local\wiki\jawiki\20260501\jawiki-20260501-pages-articles-multistream-index.txt.bz2
 * Index file size: 31,015,361
 * Index file last update: 2026-05-02T20:59:38
 * count: 3,076,894
 * </pre>
 *
 * @author nlp4j
 */
public class WikiDumpIndexPropertyPrinter {

	/**
	 * Main method - Entry point of the program
	 * メインメソッド - プログラムのエントリーポイント
	 *
	 * @param args Command-line arguments (コマンドライン引数)
	 *             --input <file.txt.bz2> : Path to the index file (インデックスファイルのパス)
	 * @throws Exception If an error occurs during file processing (ファイル処理中にエラーが発生した場合)
	 */
	public static void main(String[] args) throws Exception {
		String inputFilePath = null;

		// Parse command-line arguments
		// コマンドライン引数を解析
		for (int i = 0; i < args.length; i++) {
			if ("--input".equals(args[i]) && i + 1 < args.length) {
				inputFilePath = args[i + 1];
				i++; // Skip next argument as it's the value
				     // 次の引数は値なのでスキップ
			}
		}

		// If no input specified, show usage and exit
		// 入力が指定されていない場合、使用方法を表示して終了
		if (inputFilePath == null || inputFilePath.isEmpty()) {
			System.err.println("Usage: java WikiDumpIndexPropertyPrinter --input <file.txt.bz2>");
			System.err.println("Example: java WikiDumpIndexPropertyPrinter --input jawiki-20260501-pages-articles-multistream-index.txt.bz2");
			System.exit(1);
		}

		// Create File object from the specified path
		// 指定されたパスからFileオブジェクトを作成
		File indexFile = new File(inputFilePath);

		// Check if the file exists
		// ファイルが存在するかチェック
		if (indexFile.exists() == false) {
			System.err.println("Not found: " + indexFile.getAbsolutePath());
			System.exit(1);
		}

		// Print properties of the index file
		// インデックスファイルのプロパティを出力
		MediaWikiIndexFileUtils.printProperties(indexFile);
	}

}

// Expected output to System.out
//Index file name: C:\usr\local\wiki\jawiki\20260501\jawiki-20260501-pages-articles-multistream-index.txt.bz2
//Index file size: 31,015,361
//Index file last update: 2026-05-02T20:59:38
//count: 3,076,894


package examples;

import java.nio.file.Files;
import java.nio.file.Path;

import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.SearchResult;

public class Example19_IndexLoadFrom {

	public static void main(String[] args) throws Exception {
		Path indexDir = Files.createTempDirectory("test-index-builder-reopen-" + System.currentTimeMillis());
		System.out.println(indexDir.toAbsolutePath().toString());
		try {
			// --- フェーズ 1: インデックスを作成し、ディスクに保存して閉じる ---
			try (LocalSearch search = LocalSearch.builder("en").build()) {
				search.add("1", "Kyoto is a historic city.");
				search.add("2", "Tokyo is the capital of Japan.");
				search.add("3", "Paris is the capital of France.");
				search.commit();
				search.saveIndexTo(indexDir);
			}

			// --- フェーズ 2: 保存したディレクトリを loadIndexFrom() で指定して再オープン ---
			try (LocalSearch search = LocalSearch.builder("en").loadIndexFrom(indexDir).build()) {
				// 前回登録した "Kyoto" を含む文書がヒットすること
				SearchResult[] results = search.search("Kyoto", 10);
//				assertEquals(1, results.length);
//				assertEquals("1", results[0].id);

				// 前回登録した全ドキュメント数が維持されていること
				long total = search.count();
//				assertEquals(3, total);
			}
		} finally {
//			deleteRecursively(indexDir);
		}

	}

	static private void deleteRecursively(Path dir) {
		try {
			if (dir == null || !java.nio.file.Files.exists(dir)) {
				return;
			}
			java.nio.file.Files.walk(dir).sorted(java.util.Comparator.reverseOrder()).map(Path::toFile)
					.forEach(java.io.File::delete);
		} catch (Exception e) {
			// ignore cleanup errors
		}
	}

}

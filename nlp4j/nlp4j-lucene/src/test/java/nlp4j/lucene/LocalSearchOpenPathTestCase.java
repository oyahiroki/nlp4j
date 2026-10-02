package nlp4j.lucene;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;

import junit.framework.TestCase;

/**
 * Test target: nlp4j.lucene.LocalSearch.open(Path)
 *
 * <p>
 * saveIndexTo(Path) ⇔ open(Path) の往復（ラウンドトリップ）をテストします。
 * language / autoAnalyze / timeZone が完全復元されることを確認します。
 * </p>
 */
public class LocalSearchOpenPathTestCase extends TestCase {

    // -----------------------------------------------------------------------
    // saveIndexTo() → local-search.json が生成されること
    // -----------------------------------------------------------------------

    /**
     * saveIndexTo() を呼び出すと local-search.json が生成されること。
     */
    public void testSaveIndexTo_createsLocalSearchJson() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-open-jsoncreate-");
        try {
            try (LocalSearch search = LocalSearch.builder("ja")
                    .autoAnalyze(false)
                    .timeZone("Asia/Tokyo")
                    .build()) {
                search.add("1", "東京の観光スポット");
                search.commit();
                search.saveIndexTo(dir);
            }

            assertTrue("local-search.json should be created",
                    LocalSearchConfigStore.exists(dir));
        } finally {
            deleteRecursively(dir);
        }
    }

    // -----------------------------------------------------------------------
    // open(Path) — language の復元
    // -----------------------------------------------------------------------

    /**
     * "ja" で保存したインデックスを open(Path) で復元した場合、language が "ja" であること。
     */
    public void testOpen_restores_language_ja() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-open-lang-ja-");
        try {
            try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(false).build()) {
                search.add("1", "東京の観光スポット");
                search.commit();
                search.saveIndexTo(dir);
            }

            try (LocalSearch restored = LocalSearch.open(dir)) {
                LocalSearchConfig config = restored.getConfig();
                assertEquals("ja", config.getLanguage());
            }
        } finally {
            deleteRecursively(dir);
        }
    }

    /**
     * "en" で保存したインデックスを open(Path) で復元した場合、language が "en" であること。
     */
    public void testOpen_restores_language_en() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-open-lang-en-");
        try {
            try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(false).build()) {
                search.add("1", "Kyoto is a historic city.");
                search.commit();
                search.saveIndexTo(dir);
            }

            try (LocalSearch restored = LocalSearch.open(dir)) {
                LocalSearchConfig config = restored.getConfig();
                assertEquals("en", config.getLanguage());
            }
        } finally {
            deleteRecursively(dir);
        }
    }

    // -----------------------------------------------------------------------
    // open(Path) — autoAnalyze の復元
    // -----------------------------------------------------------------------

    /**
     * autoAnalyze=false で保存したインデックスを open(Path) で復元した場合、
     * autoAnalyze が false であること（デフォルト true に戻らないこと）。
     */
    public void testOpen_restores_autoAnalyze_false() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-open-aa-false-");
        try {
            try (LocalSearch search = LocalSearch.builder("ja").autoAnalyze(false).build()) {
                search.add("1", "東京");
                search.commit();
                search.saveIndexTo(dir);
            }

            try (LocalSearch restored = LocalSearch.open(dir)) {
                LocalSearchConfig config = restored.getConfig();
                assertFalse("autoAnalyze should be restored as false", config.isAutoAnalyze());
            }
        } finally {
            deleteRecursively(dir);
        }
    }

    /**
     * autoAnalyze=true で保存したインデックスを open(Path) で復元した場合、
     * autoAnalyze が true であること。
     */
    public void testOpen_restores_autoAnalyze_true() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-open-aa-true-");
        try {
            try (LocalSearch search = LocalSearch.builder("en").autoAnalyze(true).build()) {
                search.add("1", "Kyoto is a historic city.");
                search.commit();
                search.saveIndexTo(dir);
            }

            try (LocalSearch restored = LocalSearch.open(dir)) {
                LocalSearchConfig config = restored.getConfig();
                assertTrue("autoAnalyze should be restored as true", config.isAutoAnalyze());
            }
        } finally {
            deleteRecursively(dir);
        }
    }

    // -----------------------------------------------------------------------
    // open(Path) — timeZone の復元
    // -----------------------------------------------------------------------

    /**
     * timeZone="Asia/Tokyo" で保存したインデックスを open(Path) で復元した場合、
     * ZoneId が Asia/Tokyo であること。
     */
    public void testOpen_restores_timeZone_Tokyo() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-open-tz-tokyo-");
        try {
            try (LocalSearch search = LocalSearch.builder("ja")
                    .autoAnalyze(false)
                    .timeZone("Asia/Tokyo")
                    .build()) {
                search.add("1", "東京");
                search.commit();
                search.saveIndexTo(dir);
            }

            try (LocalSearch restored = LocalSearch.open(dir)) {
                LocalSearchConfig config = restored.getConfig();
                assertEquals(ZoneId.of("Asia/Tokyo"), config.getZoneId());
            }
        } finally {
            deleteRecursively(dir);
        }
    }

    /**
     * timeZone="UTC" で保存したインデックスを open(Path) で復元した場合、
     * ZoneId が UTC であること。
     */
    public void testOpen_restores_timeZone_UTC() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-open-tz-utc-");
        try {
            try (LocalSearch search = LocalSearch.builder("en")
                    .autoAnalyze(false)
                    .timeZone("UTC")
                    .build()) {
                search.add("1", "hello world");
                search.commit();
                search.saveIndexTo(dir);
            }

            try (LocalSearch restored = LocalSearch.open(dir)) {
                LocalSearchConfig config = restored.getConfig();
                assertEquals(ZoneId.of("UTC"), config.getZoneId());
            }
        } finally {
            deleteRecursively(dir);
        }
    }

    // -----------------------------------------------------------------------
    // open(Path) — ドキュメントと検索の復元
    // -----------------------------------------------------------------------

    /**
     * saveIndexTo() → open(Path) 後に前回登録したドキュメントが検索できること。
     */
    public void testOpen_searchWorks_afterRestore() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-open-search-");
        try {
            // フェーズ1: 保存
            try (LocalSearch search = LocalSearch.builder("en")
                    .autoAnalyze(false)
                    .timeZone("UTC")
                    .build()) {
                search.add("1", "Kyoto is a historic city.");
                search.add("2", "Tokyo is the capital of Japan.");
                search.add("3", "Paris is the capital of France.");
                search.commit();
                search.saveIndexTo(dir);
            }

            // フェーズ2: open(Path) で復元して検索
            try (LocalSearch restored = LocalSearch.open(dir)) {
                SearchResult[] results = restored.search("Kyoto", 10);
                assertEquals(1, results.length);
                assertEquals("1", results[0].id);

                assertEquals(3L, restored.count());
            }
        } finally {
            deleteRecursively(dir);
        }
    }

    /**
     * language="ja", autoAnalyze=false, timeZone="Asia/Tokyo" の全設定を
     * saveIndexTo() → open(Path) で完全復元できること（総合テスト）。
     */
    public void testOpen_fullRoundTrip_ja() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-open-full-ja-");
        try {
            // フェーズ1: 保存
            try (LocalSearch search = LocalSearch.builder("ja")
                    .autoAnalyze(false)
                    .timeZone("Asia/Tokyo")
                    .build()) {
                search.add("1", "東京の観光スポット");
                search.add("2", "京都の寺院と歴史");
                search.commit();
                search.saveIndexTo(dir);
            }

            // フェーズ2: open(Path) で復元
            try (LocalSearch restored = LocalSearch.open(dir)) {
                LocalSearchConfig config = restored.getConfig();

                // 設定がすべて復元されていること
                assertEquals("ja", config.getLanguage());
                assertFalse(config.isAutoAnalyze());
                assertEquals(ZoneId.of("Asia/Tokyo"), config.getZoneId());

                // 検索が動作すること
                SearchResult[] results = restored.search("東京", 10);
                assertEquals(1, results.length);
                assertEquals("1", results[0].id);

                assertEquals(2L, restored.count());
            }
        } finally {
            deleteRecursively(dir);
        }
    }

    // -----------------------------------------------------------------------
    // open(Path) — エラー系: local-search.json がない場合
    // -----------------------------------------------------------------------

    /**
     * local-search.json がないディレクトリに open(Path) を呼び出すと
     * LocalSearchException がスローされること。
     */
    public void testOpen_noConfigFile_throwsLocalSearchException() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-open-noconfig-");
        try {
            try {
                LocalSearch search = LocalSearch.open(dir);
                search.close();
                fail("LocalSearchException が期待される");
            } catch (LocalSearchException e) {
                assertTrue("例外メッセージに設定ファイル未発見の旨が含まれること",
                        e.getMessage().contains("local-search.json")
                                || e.getMessage().contains("configuration file"));
            }
        } finally {
            deleteRecursively(dir);
        }
    }

    /**
     * open(Path) のエラーメッセージにレガシー API の使用方法ヒントが含まれること。
     */
    public void testOpen_noConfigFile_errorMessageContainsHint() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-open-hint-");
        try {
            try {
                LocalSearch search = LocalSearch.open(dir);
                search.close();
                fail("LocalSearchException が期待される");
            } catch (LocalSearchException e) {
                // レガシーインデックスの利用方法がヒントとして含まれること
                assertTrue("エラーメッセージにレガシー open() のヒントが含まれること",
                        e.getMessage().contains("language")
                                || e.getMessage().contains("open"));
            }
        } finally {
            deleteRecursively(dir);
        }
    }

    // -----------------------------------------------------------------------
    // open(Path) — vectorField の復元
    // -----------------------------------------------------------------------

    /**
     * vectorField を含むインデックスを saveIndexTo() → open(Path) で復元した場合、
     * ベクトル検索が動作すること（Schema から vectorDimension が復元される）。
     */
    public void testOpen_vectorField_restoredFromSchema() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-open-vec-");
        try {
            // フェーズ1: vectorField つきで保存
            try (LocalSearch search = LocalSearch.builder("en")
                    .autoAnalyze(false)
                    .timeZone("UTC")
                    .vectorField("vector", 3)
                    .build()) {
                search.add("1", new float[]{1.0f, 0.0f, 0.0f});
                search.add("2", new float[]{0.0f, 1.0f, 0.0f});
                search.commit();
                search.saveIndexTo(dir);
            }

            // フェーズ2: open(Path) で復元してベクトル検索
            try (LocalSearch restored = LocalSearch.open(dir)) {
                assertEquals(3, restored.getVectorDimension());
                assertTrue(restored.hasVectorField());

                SearchResult[] results = restored.searchVector(new float[]{0.9f, 0.1f, 0.0f}, 5);
                assertEquals(2, results.length);
                assertEquals("1", results[0].id);
            }
        } finally {
            deleteRecursively(dir);
        }
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private void deleteRecursively(Path dir) {
        try {
            if (dir == null || !Files.exists(dir)) {
                return;
            }
            Files.walk(dir)
                    .sorted(java.util.Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
        } catch (Exception e) {
            // ignore cleanup errors
        }
    }
}

package nlp4j.lucene;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;

import junit.framework.TestCase;

/**
 * Test target: nlp4j.lucene.LocalSearchConfigStore
 */
public class LocalSearchConfigStoreTestCase extends TestCase {

    // -----------------------------------------------------------------------
    // exists()
    // -----------------------------------------------------------------------

    /**
     * local-search.json が存在しない場合、exists() が false を返すこと。
     */
    public void testExists_false_whenFileNotPresent() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-cfg-exists-");
        try {
            assertFalse(LocalSearchConfigStore.exists(dir));
        } finally {
            deleteRecursively(dir);
        }
    }

    /**
     * save() 後に exists() が true を返すこと。
     */
    public void testExists_true_afterSave() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-cfg-exists2-");
        try {
            LocalSearchConfig config = new LocalSearchConfig("ja", false, ZoneId.of("Asia/Tokyo"));
            LocalSearchConfigStore.save(dir, config);

            assertTrue(LocalSearchConfigStore.exists(dir));
        } finally {
            deleteRecursively(dir);
        }
    }

    // -----------------------------------------------------------------------
    // save() → load() ラウンドトリップ
    // -----------------------------------------------------------------------

    /**
     * language="ja", autoAnalyze=false, timeZone="Asia/Tokyo" で保存して
     * 読み込んだ場合、値がすべて復元されること。
     */
    public void testSaveAndLoad_ja_autoAnalyzeFalse_Tokyo() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-cfg-roundtrip-");
        try {
            LocalSearchConfig original = new LocalSearchConfig(
                    "ja", false, ZoneId.of("Asia/Tokyo"));

            LocalSearchConfigStore.save(dir, original);
            LocalSearchConfig loaded = LocalSearchConfigStore.load(dir);

            assertEquals("ja", loaded.getLanguage());
            assertFalse(loaded.isAutoAnalyze());
            assertEquals(ZoneId.of("Asia/Tokyo"), loaded.getZoneId());
        } finally {
            deleteRecursively(dir);
        }
    }

    /**
     * language="en", autoAnalyze=true, timeZone="UTC" で保存して
     * 読み込んだ場合、値がすべて復元されること。
     */
    public void testSaveAndLoad_en_autoAnalyzeTrue_UTC() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-cfg-roundtrip2-");
        try {
            LocalSearchConfig original = new LocalSearchConfig(
                    "en", true, ZoneId.of("UTC"));

            LocalSearchConfigStore.save(dir, original);
            LocalSearchConfig loaded = LocalSearchConfigStore.load(dir);

            assertEquals("en", loaded.getLanguage());
            assertTrue(loaded.isAutoAnalyze());
            assertEquals(ZoneId.of("UTC"), loaded.getZoneId());
        } finally {
            deleteRecursively(dir);
        }
    }

    /**
     * アメリカ/ニューヨークのタイムゾーンが正しく保存・復元されること。
     */
    public void testSaveAndLoad_timezone_America_NewYork() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-cfg-tz-");
        try {
            LocalSearchConfig original = new LocalSearchConfig(
                    "en", true, ZoneId.of("America/New_York"));

            LocalSearchConfigStore.save(dir, original);
            LocalSearchConfig loaded = LocalSearchConfigStore.load(dir);

            assertEquals(ZoneId.of("America/New_York"), loaded.getZoneId());
        } finally {
            deleteRecursively(dir);
        }
    }

    // -----------------------------------------------------------------------
    // 保存されるファイルの内容確認
    // -----------------------------------------------------------------------

    /**
     * 保存されるファイル名が "local-search.json" であること（FILE_NAME 定数を確認）。
     */
    public void testFileNameConstant() {
        assertEquals("local-search.json", LocalSearchConfigStore.FILE_NAME);
    }

    /**
     * save() 後に local-search.json が生成されており、version フィールドが含まれること。
     */
    public void testSave_fileContainsVersion() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-cfg-version-");
        try {
            LocalSearchConfig config = new LocalSearchConfig("ja", true, ZoneId.of("UTC"));
            LocalSearchConfigStore.save(dir, config);

            String content = Files.readString(
                    dir.resolve(LocalSearchConfigStore.FILE_NAME),
                    StandardCharsets.UTF_8);

            assertTrue("JSON should contain 'version'", content.contains("version"));
            assertTrue("JSON should contain 'language'", content.contains("language"));
            assertTrue("JSON should contain 'autoAnalyze'", content.contains("autoAnalyze"));
            assertTrue("JSON should contain 'timeZone'", content.contains("timeZone"));
        } finally {
            deleteRecursively(dir);
        }
    }

    /**
     * save() を 2 回呼び出した場合、2 回目の内容で上書きされること。
     */
    public void testSave_overwrite() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-cfg-overwrite-");
        try {
            LocalSearchConfig first = new LocalSearchConfig("ja", false, ZoneId.of("Asia/Tokyo"));
            LocalSearchConfigStore.save(dir, first);

            LocalSearchConfig second = new LocalSearchConfig("en", true, ZoneId.of("UTC"));
            LocalSearchConfigStore.save(dir, second);

            LocalSearchConfig loaded = LocalSearchConfigStore.load(dir);

            assertEquals("en", loaded.getLanguage());
            assertTrue(loaded.isAutoAnalyze());
            assertEquals(ZoneId.of("UTC"), loaded.getZoneId());
        } finally {
            deleteRecursively(dir);
        }
    }

    // -----------------------------------------------------------------------
    // version 検証
    // -----------------------------------------------------------------------

    /**
     * version=1 (CURRENT_VERSION) で保存・ロードした場合は正常に動作すること。
     */
    public void testLoad_version1_OK() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-cfg-ver1-");
        try {
            LocalSearchConfig config = new LocalSearchConfig("ja", false, ZoneId.of("Asia/Tokyo"));
            LocalSearchConfigStore.save(dir, config);

            // バージョン一致 → 例外なしでロードできること
            LocalSearchConfig loaded = LocalSearchConfigStore.load(dir);
            assertEquals("ja", loaded.getLanguage());
        } finally {
            deleteRecursively(dir);
        }
    }

    /**
     * version フィールドにサポート外の値（例: 999）が書かれている場合、
     * load() が IOException をスローすること。
     */
    public void testLoad_unsupportedVersion_throwsIOException() throws Exception {
        Path dir = Files.createTempDirectory("nlp4j-test-cfg-badver-");
        try {
            // 直接 version=999 のファイルを書き込む
            String badJson = "{\"version\":999,\"language\":\"ja\",\"autoAnalyze\":false,\"timeZone\":\"Asia/Tokyo\"}";
            Files.writeString(
                    dir.resolve(LocalSearchConfigStore.FILE_NAME),
                    badJson,
                    java.nio.charset.StandardCharsets.UTF_8);

            try {
                LocalSearchConfigStore.load(dir);
                fail("IOException が期待される");
            } catch (java.io.IOException e) {
                assertTrue("例外メッセージに version 番号が含まれること",
                        e.getMessage().contains("999"));
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

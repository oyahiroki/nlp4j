package nlp4j.lucene;

import java.time.ZoneId;

import junit.framework.TestCase;

/**
 * Test target: nlp4j.lucene.LocalSearchConfig
 */
public class LocalSearchConfigTestCase extends TestCase {

    // -----------------------------------------------------------------------
    // コンストラクタ — 正常系
    // -----------------------------------------------------------------------

    /**
     * 正常な引数で LocalSearchConfig が生成されること。
     */
    public void testConstructor_valid() {
        LocalSearchConfig config = new LocalSearchConfig("ja", false, ZoneId.of("Asia/Tokyo"));

        assertEquals("ja", config.getLanguage());
        assertFalse(config.isAutoAnalyze());
        assertEquals(ZoneId.of("Asia/Tokyo"), config.getZoneId());
    }

    /**
     * autoAnalyze=true で生成した場合、isAutoAnalyze() が true を返すこと。
     */
    public void testConstructor_autoAnalyzeTrue() {
        LocalSearchConfig config = new LocalSearchConfig("en", true, ZoneId.of("UTC"));

        assertTrue(config.isAutoAnalyze());
        assertEquals("en", config.getLanguage());
        assertEquals(ZoneId.of("UTC"), config.getZoneId());
    }

    /**
     * ZoneId.systemDefault() を指定して生成できること。
     */
    public void testConstructor_systemDefaultZone() {
        ZoneId systemZone = ZoneId.systemDefault();
        LocalSearchConfig config = new LocalSearchConfig("en", true, systemZone);

        assertEquals(systemZone, config.getZoneId());
    }

    // -----------------------------------------------------------------------
    // コンストラクタ — 異常系
    // -----------------------------------------------------------------------

    /**
     * language が null の場合 IllegalArgumentException がスローされること。
     */
    public void testConstructor_nullLanguage_throws() {
        try {
            new LocalSearchConfig(null, true, ZoneId.of("UTC"));
            fail("IllegalArgumentException が期待される");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("language"));
        }
    }

    /**
     * language が空文字列の場合 IllegalArgumentException がスローされること。
     */
    public void testConstructor_emptyLanguage_throws() {
        try {
            new LocalSearchConfig("", true, ZoneId.of("UTC"));
            fail("IllegalArgumentException が期待される");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("language"));
        }
    }

    /**
     * language が空白のみの場合 IllegalArgumentException がスローされること。
     */
    public void testConstructor_blankLanguage_throws() {
        try {
            new LocalSearchConfig("   ", true, ZoneId.of("UTC"));
            fail("IllegalArgumentException が期待される");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("language"));
        }
    }

    /**
     * zoneId が null の場合 IllegalArgumentException がスローされること。
     */
    public void testConstructor_nullZoneId_throws() {
        try {
            new LocalSearchConfig("ja", true, null);
            fail("IllegalArgumentException が期待される");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("zoneId"));
        }
    }

    // -----------------------------------------------------------------------
    // toString()
    // -----------------------------------------------------------------------

    /**
     * toString() に language / autoAnalyze / zoneId が含まれること。
     */
    public void testToString_containsFields() {
        LocalSearchConfig config = new LocalSearchConfig("ja", false, ZoneId.of("Asia/Tokyo"));
        String str = config.toString();

        assertTrue("toString should contain language", str.contains("ja"));
        assertTrue("toString should contain autoAnalyze", str.contains("false"));
        assertTrue("toString should contain zoneId", str.contains("Asia/Tokyo"));
    }

    // -----------------------------------------------------------------------
    // LocalSearch.getConfig() との連動
    // -----------------------------------------------------------------------

    /**
     * LocalSearch.builder("ja").autoAnalyze(false).timeZone("Asia/Tokyo") で生成した
     * LocalSearch から getConfig() を呼び出すと、設定が正しく返されること。
     */
    public void testGetConfig_fromLocalSearch() throws Exception {
        try (LocalSearch search = LocalSearch.builder("ja")
                .autoAnalyze(false)
                .timeZone("Asia/Tokyo")
                .build()) {

            LocalSearchConfig config = search.getConfig();

            assertEquals("ja", config.getLanguage());
            assertFalse(config.isAutoAnalyze());
            assertEquals(ZoneId.of("Asia/Tokyo"), config.getZoneId());
        }
    }

    /**
     * デフォルト設定（autoAnalyze=true, systemDefault timezone）で生成した LocalSearch の
     * getConfig() が正しい値を返すこと。
     */
    public void testGetConfig_defaults() throws Exception {
        ZoneId systemZone = ZoneId.systemDefault();

        try (LocalSearch search = LocalSearch.builder("en").build()) {
            LocalSearchConfig config = search.getConfig();

            assertEquals("en", config.getLanguage());
            assertTrue(config.isAutoAnalyze());
            assertEquals(systemZone, config.getZoneId());
        }
    }
}

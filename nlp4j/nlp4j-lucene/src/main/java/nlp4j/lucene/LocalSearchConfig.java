/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene;

import java.time.ZoneId;

/**
 * LocalSearch の永続化可能な動作設定。
 *
 * <p>
 * {@link nlp4j.lucene10.SearchSchema} がインデックスのフィールド構造を表すのに対し、
 * {@code LocalSearchConfig} は LocalSearch 自体の動作設定（language / autoAnalyze /
 * timeZone）を表します。
 * </p>
 *
 * <p>
 * {@link LocalSearchConfigStore} によって {@code local-search.json} に保存・読み込みされます。
 * </p>
 */
public class LocalSearchConfig {

    private final String language;
    private final boolean autoAnalyze;
    private final ZoneId zoneId;

    /**
     * @param language    言語コード（null または空白は不可）
     * @param autoAnalyze 自動解析の有効・無効
     * @param zoneId      タイムゾーン（null 不可）
     * @throws IllegalArgumentException language が null / blank、または zoneId が null の場合
     */
    public LocalSearchConfig(
            String language,
            boolean autoAnalyze,
            ZoneId zoneId) {

        if (language == null || language.isBlank()) {
            throw new IllegalArgumentException("language must not be blank");
        }
        if (zoneId == null) {
            throw new IllegalArgumentException("zoneId must not be null");
        }

        this.language = language;
        this.autoAnalyze = autoAnalyze;
        this.zoneId = zoneId;
    }

    /** @return 言語コード */
    public String getLanguage() {
        return language;
    }

    /** @return 自動解析が有効の場合 {@code true} */
    public boolean isAutoAnalyze() {
        return autoAnalyze;
    }

    /** @return タイムゾーン */
    public ZoneId getZoneId() {
        return zoneId;
    }

    @Override
    public String toString() {
        return "LocalSearchConfig [language=" + language
                + ", autoAnalyze=" + autoAnalyze
                + ", zoneId=" + zoneId + "]";
    }
}

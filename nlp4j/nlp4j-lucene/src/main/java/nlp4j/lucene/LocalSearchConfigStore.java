/*
 * Copyright (C) 2026 Hiroki OYA
 *
 * Licensed under the Apache License, Version 2.0
 */
package nlp4j.lucene;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * {@link LocalSearchConfig} をディスクへ保存・読み込みするユーティリティクラス。
 *
 * <p>
 * インデックスディレクトリ内の {@value #FILE_NAME} ファイルを JSON 形式で読み書きします。
 * {@link nlp4j.lucene10.SearchSchemaStore} と対になる設計です。
 * </p>
 *
 * <pre>
 * // 保存
 * LocalSearchConfigStore.save(indexDir, config);
 *
 * // 存在確認
 * if (LocalSearchConfigStore.exists(indexDir)) { ... }
 *
 * // 読み込み
 * LocalSearchConfig config = LocalSearchConfigStore.load(indexDir);
 * </pre>
 */
public final class LocalSearchConfigStore {

    /** 設定ファイル名 */
    public static final String FILE_NAME = "local-search.json";

    /** 現在サポートする設定ファイルのバージョン */
    private static final int CURRENT_VERSION = 1;

    private LocalSearchConfigStore() {
    }

    /**
     * 指定ディレクトリに {@value #FILE_NAME} が存在するかどうかを返します。
     *
     * @param indexDir インデックスディレクトリ
     * @return ファイルが存在する場合 {@code true}
     */
    public static boolean exists(Path indexDir) {
        return Files.exists(indexDir.resolve(FILE_NAME));
    }

    /**
     * {@link LocalSearchConfig} をインデックスディレクトリに保存します。
     *
     * <p>
     * 保存形式:
     * </p>
     *
     * <pre>
     * {
     *   "version": 1,
     *   "language": "ja",
     *   "autoAnalyze": false,
     *   "timeZone": "Asia/Tokyo"
     * }
     * </pre>
     *
     * @param indexDir インデックスディレクトリ
     * @param config   保存する設定
     * @throws IOException 書き込みに失敗した場合
     */
    public static void save(Path indexDir, LocalSearchConfig config) throws IOException {

        JsonObject json = new JsonObject();

        json.addProperty("version", CURRENT_VERSION);
        json.addProperty("language", config.getLanguage());
        json.addProperty("autoAnalyze", config.isAutoAnalyze());
        json.addProperty("timeZone", config.getZoneId().getId());

        Files.writeString(
                indexDir.resolve(FILE_NAME),
                json.toString(),
                StandardCharsets.UTF_8);
    }

    /**
     * インデックスディレクトリから {@link LocalSearchConfig} を読み込みます。
     *
     * @param indexDir インデックスディレクトリ
     * @return 読み込んだ設定
     * @throws IOException ファイルの読み込みまたは解析に失敗した場合
     */
    public static LocalSearchConfig load(Path indexDir) throws IOException {

        String text = Files.readString(
                indexDir.resolve(FILE_NAME),
                StandardCharsets.UTF_8);

        JsonObject json = JsonParser.parseString(text).getAsJsonObject();

        int version = json.get("version").getAsInt();
        if (version != CURRENT_VERSION) {
            throw new IOException(
                    "Unsupported LocalSearch config version: " + version);
        }

        String language = json.get("language").getAsString();
        boolean autoAnalyze = json.get("autoAnalyze").getAsBoolean();
        ZoneId zoneId = ZoneId.of(json.get("timeZone").getAsString());

        return new LocalSearchConfig(language, autoAnalyze, zoneId);
    }
}

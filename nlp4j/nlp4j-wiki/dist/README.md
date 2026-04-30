以下に、そのまま使える **README.md** を用意しました。
（英語 → 日本語の順です）

---

````markdown
# wikidump-sampler

A simple and fast command-line tool to extract a specified number of documents from a Wikipedia dump file and output them in JSONL format.

---

## Overview

Wikipedia dump files are large and difficult to handle directly.  
This tool allows you to:

- Extract a fixed number of articles
- Convert them into JSONL format
- Stream output to a file or stdout

This is useful for:

- RAG (Retrieval-Augmented Generation) dataset creation
- Search engine testing (Elasticsearch / OpenSearch / Solr)
- Embedding experiments
- NLP / LLM evaluation

---

## Features

- Stream processing (low memory usage)
- Early termination after N documents
- JSONL output (one JSON object per line)
- Supports stdout (pipe-friendly)
- Simple CLI interface

---

## Download

Download the JAR file from:

https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-wiki/dist

---

## Usage

```bash
java -jar wikidump-sampler.jar \
  --input <dump_file> \
  --count <number> \
  [--output <file>]
````

### Required Options

| Option    | Description                            |
| --------- | -------------------------------------- |
| `--input` | Wikipedia dump file (e.g., `.xml.bz2`) |
| `--count` | Number of documents to extract         |

### Optional Options

| Option     | Description                                       |
| ---------- | ------------------------------------------------- |
| `--output` | Output file (default: stdout, use `-` for stdout) |

---

## Examples

### Extract 10 documents (stdout)

```bash
java -jar wikidump-sampler.jar \
  --input jawiki-latest-pages-articles.xml.bz2 \
  --count 10
```

---

### Extract 1,000,000 documents to file

```bash
java -jar wikidump-sampler.jar \
  --input jawiki-latest-pages-articles.xml.bz2 \
  --count 1000000 \
  --output sample.jsonl
```

---

### Pipe to another command

```bash
java -jar wikidump-sampler.jar \
  --input jawiki.xml.bz2 \
  --count 1000 \
  --output - | head
```

---

## Output Format (JSONL)

Each line is a JSON object:

```json
{"timestamp":"2026-04-01T00:00:00Z","title":"Tokyo","text":"..."}
```

Fields:

* `timestamp`: revision timestamp
* `title`: page title
* `text`: extracted plain text

---

## Notes

* Only main namespace articles are effectively targeted (depending on parser behavior)
* Redirect pages may be included
* Large pages are included as-is

---

## License

This project is part of the nlp4j ecosystem.

---

# wikidump-sampler（日本語）

Wikipediaのダンプファイルから、指定した件数の文書を抽出し、JSONL形式で出力するシンプルなコマンドラインツールです。

---

## 概要

Wikipediaのダンプファイルは非常に大きく、そのまま扱うのは困難です。
本ツールでは以下が可能です：

* 指定件数の文書を抽出
* JSONL形式に変換
* ファイルまたは標準出力へストリーム出力

主な用途：

* RAG（検索拡張生成）のデータ作成
* 検索エンジン（Elasticsearch / OpenSearch / Solr）の検証
* Embeddingのテスト
* NLP / LLMの評価用データ作成

---

## 特徴

* ストリーミング処理（低メモリ）
* 指定件数で即終了
* JSONL形式出力（1行1JSON）
* stdout対応（パイプ処理可能）
* シンプルなCLI

---

## ダウンロード

以下からJARファイルを取得してください：

[https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-wiki/dist](https://github.com/oyahiroki/nlp4j/tree/master/nlp4j/nlp4j-wiki/dist)

---

## 使い方

```bash
java -jar wikidump-sampler.jar \
  --input <ダンプファイル> \
  --count <件数> \
  [--output <出力ファイル>]
```

### 必須オプション

| オプション     | 説明                             |
| --------- | ------------------------------ |
| `--input` | Wikipediaダンプファイル（例：`.xml.bz2`） |
| `--count` | 抽出する件数                         |

---

### オプション

| オプション      | 説明                            |
| ---------- | ----------------------------- |
| `--output` | 出力ファイル（省略時は標準出力、`-`でstdout指定） |

---

## 実行例

### 10件抽出（標準出力）

```bash
java -jar wikidump-sampler.jar \
  --input jawiki-latest-pages-articles.xml.bz2 \
  --count 10
```

---

### 100万件をファイル出力

```bash
java -jar wikidump-sampler.jar \
  --input jawiki-latest-pages-articles.xml.bz2 \
  --count 1000000 \
  --output sample.jsonl
```

---

### パイプ処理

```bash
java -jar wikidump-sampler.jar \
  --input jawiki.xml.bz2 \
  --count 1000 \
  --output - | head
```

---

## 出力形式（JSONL）

1行ごとに1つのJSON：

```json
{"timestamp":"2026-04-01T00:00:00Z","title":"東京","text":"..."}
```

項目：

* `timestamp`：更新日時
* `title`：ページタイトル
* `text`：抽出された本文（プレーンテキスト）

---

## 注意事項

* 主に記事（namespace=0）が対象となります
* リダイレクトページが含まれる場合があります
* 大きなページもそのまま出力されます

---

## ライセンス

本ツールは nlp4j プロジェクトの一部です。

```

---

必要であれば、

- バッジ（GitHub / license / release）
- GIFデモ
- 「なぜこのツールが必要か」のストーリー

を追加して、さらに“使われるREADME”に仕上げることもできます 👍
```

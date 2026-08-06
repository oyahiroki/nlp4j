# nlp4j-lucene ビルド・リリース手順

## 前提条件

| 項目 | バージョン |
|---|---|
| Java | 17 以上 |
| Maven | 3.8 以上 |
| GPG | 任意（Maven Central への公開時のみ必要） |

---

## 日常開発コマンド

### コンパイル

```
mvn compile
```

### テスト実行

```
mvn test
```

特定のテストクラスのみ実行する場合：

```
mvn test -Dtest=LocalSearchTestCase
```

特定のテストメソッドのみ実行する場合：

```
mvn test -Dtest=LocalSearchTestCase#testMultiValuedAggregation001
```

ワイルドカードでまとめて実行する場合：

```
mvn test -Dtest=LocalSearchTestCase#testMultiValued*
```

### テストをスキップしてパッケージ作成

```
mvn package -Dmaven.test.skip
```

### ローカルリポジトリへインストール

```
mvn install
```

### Javadoc 生成

```
mvn javadoc:javadoc
```

出力先: `target/site/apidocs/`

---

## 依存関係の確認

### 依存ツリー表示

```
mvn dependency:tree
```

### 依存 JAR を `target/dependency/` にコピー

```
mvn dependency:copy-dependencies
```

出力先: `./target/dependency/`

特定ディレクトリに出力する場合：

```
mvn dependency:copy-dependencies -DoutputDirectory=lib
```

---

## バージョン管理

バージョンは `pom.xml` の `<version>` タグで管理しています。

```xml
<!-- pom.xml -->
<artifactId>nlp4j-lucene</artifactId>
<version>1.4.0.0</version>
```

リリース前に必ずバージョンを更新してください。
すでに Maven Central に公開済みのバージョンは上書きできません（後述）。

バージョン体系: `メジャー.マイナー.パッチ.ビルド`

---

## Maven Central への公開

### 事前準備

#### 1. GPG 鍵の確認

```
gpg --list-keys
```

鍵が存在しない場合は生成してください：

```
gpg --gen-key
```

生成した鍵を鍵サーバーに公開してください：

```
gpg --keyserver keyserver.ubuntu.com --send-keys <KEY_ID>
```

#### 2. GPG 署名の動作確認

```
echo test | gpg --clearsign
```

パスフレーズの入力を求められれば正常です。

#### 3. `~/.m2/settings.xml` の確認

Maven Central への認証情報が設定されていることを確認してください：

```xml
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>YOUR_SONATYPE_USERNAME</username>
      <password>YOUR_SONATYPE_PASSWORD</password>
    </server>
  </servers>
</settings>
```

### リリース実行

```
mvn -DperformRelease=true clean deploy
```

詳細ログを出力しながら実行する場合：

```
mvn -e -X -DperformRelease=true clean deploy
```

- `-e` : エラー時にスタックトレースを表示
- `-X` : デバッグログを出力
- `-DperformRelease=true` : 親 pom のリリース用プロファイルを有効化（GPG 署名、sources/javadoc JAR の添付）

---

## よくあるエラーと対処

### すでに公開済みバージョンを再デプロイしようとした場合

Maven Central はリリース済みアーティファクトの上書きを禁止しています。
`pom.xml` のバージョンを上げてから再度デプロイしてください。

```
[ERROR] Nexus Staging Rules Failure Report
[ERROR] Rule "RepositoryWritePolicy" failures
[ERROR]   * Artifact updating: Repository ='releases:Releases' does not allow updating artifact=
          '/org/nlp4j/nlp4j-lucene/1.x.x.x/nlp4j-lucene-1.x.x.x.jar'
```

**対処**: `pom.xml` の `<version>` を新しい値に更新してからリリースしてください。

---

### GPG 署名に失敗する場合

```
gpg: signing failed: No secret key
```

**対処**: GPG 鍵が存在するか確認し、必要に応じて生成・登録してください。

```
gpg --list-secret-keys
```

---

### プロファイル `deploy` が見つからない警告

```
[WARNING] The requested profile "deploy" could not be activated because it does not exist.
```

この警告は無害です。リリース用プロファイルは `-DperformRelease=true` で有効化されます。

---

## Maven Central でのアーティファクト確認

公開後は以下の URL で確認できます。

- **Maven Central (新UI)**: https://central.sonatype.com/artifact/org.nlp4j/nlp4j-lucene/
- **Maven Central (旧UI)**: https://oss.sonatype.org/

---

## 関連リンク

- [Maven Central 公開ガイド](https://central.sonatype.org/publish/publish-guide/)
- [Apache Lucene](https://lucene.apache.org/)
- [nlp4j プロジェクト](https://nlp4j.org/)

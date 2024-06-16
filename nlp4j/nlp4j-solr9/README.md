#NLP4J Solr9

## Solr バージョン

9.3.0

## ダウンロード

https://solr.apache.org/downloads.html

## 公式チュートリアル

https://solr.apache.org/guide/solr/latest/getting-started/solr-tutorial.html

## インストール(local)

以下ディレクトリに展開

/usr/local/solr-9.3.0

### 環境変数JAVA_HOME

```
C:\usr\local\solr-9.3.0>bin\solr
エラー: 指定されたレジストリ キーまたは値が見つかりませんでした
エラー: 指定されたレジストリ キーまたは値が見つかりませんでした
Please set the JAVA_HOME environment variable to the path where you installed Java 1.8+
```

### 起動 start

```
> bin\solr start
```

### 起動 stop

```
> bin\solr stop -all
```

### コアの作成 create core

```
> bin\solr create_core -c sandbox
```

server/solr/sandbox が作成される

server/solr/sandbox/conf/managed-schema.xml を編集してフィールドを定義する


### フィールドの定義

managed-schema.xml に以下の定義を追加する。（手作業でないとできないっぽい）

#### フィールドの定義

```
<field name="vector" type="knn_vector" indexed="true" stored="false" /> <!-- フィールド定義 -->
```

#### フィールドタイプ(型)の定義

```
<fieldType name="knn_vector" class="solr.DenseVectorField" vectorDimension="4" similarityFunction="cosine" /> <!-- 型定義 -->
```

vectorDimension: ベクトルの次元数
similarityFunction: 類似計算の方法



## 参照

Apache Solr Reference Guide (9.3) - Dense Vector Search

https://solr.apache.org/guide/solr/latest/query-guide/dense-vector-search.html

Solr9 から導入されたベクトル検索ってどんな感じ？

https://zenn.dev/sashimimochi/articles/1957974d64d571

Solr9 から導入されたベクトル検索ってどうなのよ？

https://qiita.com/Sashimimochi/items/b52e9ea80f54d7e451ce


## インストール(docker)

```
>docker run --name solr_9_3 -p 8983:8983 -it solr:9.3 solr-precreate gettingstarted
```

# HelloVector1 (Direction)

２次元ベクトルで「方向」をあらわして試してみる

コレクションを作成する

```
>bin\solr create_core -c hellovector_1_direction
```

managed-schema.xml

```
<field name="vector" type="knn_vector" indexed="true" stored="false" /> <!-- フィールド定義 -->
```

２次元ベクトル

```
<fieldType name="knn_vector" class="solr.DenseVectorField" vectorDimension="2" similarityFunction="cosine" /> <!-- 型定義 -->
```




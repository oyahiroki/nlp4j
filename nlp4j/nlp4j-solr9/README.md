#NLP4J Solr9

## 対象バージョン

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

## インストール(docker)

```
>docker run --name solr_9_3 -p 8983:8983 -it solr:9.3 solr-precreate gettingstarted
```



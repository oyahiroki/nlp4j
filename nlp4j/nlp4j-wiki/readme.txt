This is memo for nlp4j-wiki

ファイルの展開

Cygwin
$ bzip2 -d cygdrive/c/usr/local/data/jawiki-20210401-pages-articles-multistream-index.txt.bz2

WSL2
$ bzip2 -d /mnt/c/usr/local/data/wiki/jawiki-20210401-pages-articles-multistream.xml.bz2

TODO
ツール化する
	環境変数でIndexファイルを指定しておく
	コマンドでクエリー


Maven
mvn test
mvn javadoc:javadoc
mvn -DperformRelease=true clean deploy

mvn assembly:assembly -DdescriptorId=jar-with-dependencies
java -Dfile.encoding=UTF-8 -jar target\nlp4j-wiki-0.1.1.0-jar-with-dependencies.jar
java -Dfile.encoding=UTF-8 -jar target\nlp4j-wiki-0.1.1.0-jar-with-dependencies.jar
java -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -Dsun.stdin.encoding=UTF-8 -cp target\nlp4j-wiki-0.1.1.0-jar-with-dependencies.jar nlp4j.wiki.WikiMain
java -cp target\nlp4j-wiki-0.1.1.0-jar-with-dependencies.jar nlp4j.wiki.WikiMain

=={{ja}}==
日本語
==={{noun}}===
名詞
==={{adj}}===
形容詞
===={{pron|jpn}}====
活用
===={{rel}}====
関連語
→リンクを取得
===={{etym}}====
語源
===={{trans}}====
翻訳
=={{zho}}==
中国語
==={{noun}}===
名詞

===={{syn}}====
類義語
===={{ant}}====
対義語

==={{adj}}：醜い===
==={{adj}}：見難い===

ISO 639-1コード一覧 - Wikipedia
https://ja.wikipedia.org/wiki/ISO_639-1%E3%82%B3%E3%83%BC%E3%83%89%E4%B8%80%E8%A6%A7

テンプレート:言語コード - ウィクショナリー日本語版
https://ja.wiktionary.org/wiki/%E3%83%86%E3%83%B3%E3%83%97%E3%83%AC%E3%83%BC%E3%83%88:%E8%A8%80%E8%AA%9E%E3%82%B3%E3%83%BC%E3%83%89

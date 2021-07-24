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


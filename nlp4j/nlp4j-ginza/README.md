# NLP4J for GiNZA - GiNZA をJavaから利用する

## GiNZA Official Site

GiNZA - Japanese NLP Library

Universal Dependenciesに基づくオープンソース日本語NLPライブラリ

https://megagonlabs.github.io/ginza/

# How to install GiNZA - GiNZAのインストール方法

```
$ pip install -U ginza ja_ginza_electra
```

詳しくは公式ページ https://megagonlabs.github.io/ginza/ を参照

```
$ nvcc -V
Command 'nvcc' not found, but can be installed with:
sudo apt install nvidia-cuda-toolkit

$ sudo apt install nvidia-cuda-toolkit

...

$ nvcc -V
nvcc: NVIDIA (R) Cuda compiler driver
Copyright (c) 2005-2021 NVIDIA Corporation
Built on Thu_Nov_18_09:45:30_PST_2021
Cuda compilation tools, release 11.5, V11.5.119
Build cuda_11.5.r11.5/compiler.30672275_0
```

```
$ ginza
今日はいい天気です。走って学校に行きます。
# text = 今日はいい天気です。
1       今日    今日    NOUN    名詞-普通名詞-副詞可能  _       4       obl     _       SpaceAfter=No|BunsetuBILabel=B|BunsetuPositionType=SEM_HEAD|NP_B|Reading=キョウ
2       は      は      ADP     助詞-係助詞     _       1       case    _       SpaceAfter=No|BunsetuBILabel=I|BunsetuPositionType=SYN_HEAD|Reading=ハ
3       いい    いい    ADJ     形容詞-非自立可能       _       4       acl     _       SpaceAfter=No|BunsetuBILabel=B|BunsetuPositionType=SEM_HEAD|Inf=形容詞,連体形-一般|Reading=イイ
4       天気    天気    NOUN    名詞-普通名詞-一般      _       0       root    _       SpaceAfter=No|BunsetuBILabel=B|BunsetuPositionType=ROOT|NP_B|Reading=テンキ
5       です    です    AUX     助動詞  _       4       cop     _       SpaceAfter=No|BunsetuBILabel=I|BunsetuPositionType=SYN_HEAD|Inf=助動詞-デス,終止形-一般|Reading=デス
6       。      。      PUNCT   補助記号-句点   _       4       punct   _       SpaceAfter=No|BunsetuBILabel=I|BunsetuPositionType=CONT|Reading=。

# text = 走って学校に行きます。
1       走っ    走る    VERB    動詞-一般       _       5       advcl   _       SpaceAfter=No|BunsetuBILabel=B|BunsetuPositionType=SEM_HEAD|Inf=五段-ラ行,連用形-促音便|Reading=ハシッ
2       て      て      SCONJ   助詞-接続助詞   _       1       mark    _       SpaceAfter=No|BunsetuBILabel=I|BunsetuPositionType=SYN_HEAD|Reading=テ
3       学校    学校    NOUN    名詞-普通名詞-一般      _       5       obl     _       SpaceAfter=No|BunsetuBILabel=B|BunsetuPositionType=SEM_HEAD|NP_I|Reading=ガッコウ
4       に      に      ADP     助詞-格助詞     _       3       case    _       SpaceAfter=No|BunsetuBILabel=I|BunsetuPositionType=SYN_HEAD|Reading=ニ
5       行き    行く    VERB    動詞-非自立可能 _       0       root    _       SpaceAfter=No|BunsetuBILabel=B|BunsetuPositionType=ROOT|Inf=五段-カ行,連用形-一般|Reading=イキ
6       ます    ます    AUX     助動詞  _       5       aux     _       SpaceAfter=No|BunsetuBILabel=I|BunsetuPositionType=SYN_HEAD|Inf=助動詞-マス,終止形-一般|Reading=マス
7       。      。      PUNCT   補助記号-句点   _       5       punct   _       SpaceAfter=No|BunsetuBILabel=I|BunsetuPositionType=CONT|Reading=。
```

# How to run GiNZA server - GiNZA Server を起動する

```
python nlp4j-ginza-ginzaserver\ginzaserver.py
```

Ctrl + C で終了

# Install GiNZA Server - GiNZA Server をインストールする

```
pip install git+https://github.com/oyahiroki/ginzaserver
```


# Universal Dependency 関連論文

https://www.jstage.jst.go.jp/article/jnlp/26/1/26_3/_pdf

# POS Map - 品詞の対応

```
POS	意味
ADJ	形容詞
ADP	設置詞
ADV	副詞
AUX	助動詞
CCONJ	接続詞
DET	限定詞
INTJ	間投詞
NOUN	名詞
NUM	数詞
PART	助詞
PRON	代名詞
PROPN	固有名詞
PUNCT	句読点
SCONJ	連結詞
SYM	シンボル
VERB	動詞
X	その他
```

<!--
```
POS_JP_DICT = {
    'NOUN': '名詞',
    'PROPN': '固有名詞',
    'VERB': '動詞',
    'ADJ': '形容詞',
    'ADV': '副詞',
    'CCONJ': '接続詞',
    'INTJ': '間投詞',
    'PRON': '代名詞',
    'NUM': '数詞',
    'AUX': '助動詞',
    'CONJ': '接続詞',
    'SCONJ': '従属接続詞',
    'DET': '限定詞',
    'ADP': '接置詞',
    'PART': '接辞',
    'PUNCT': '句読点',
    'SYM': '記号',
    'X': 'その他'
```
-->

# Dependency Relations - 係り受け

```
係り受けタグ	意味
acl	名詞の節修飾語
advcl	副詞節修飾語
advmod	副詞修飾語
amod	形容詞修飾語
appos	同格
aux	助動詞
case	格表示
cc	等位接続詞
ccomp	補文
clf	類別詞
compound	複合名詞
conj	結合詞
cop	連結詞
csubj	主部
dep	不明な依存関係
det	限定詞
discourse	談話要素
dislocated	転置
expl	嘘辞
fixed	固定複数単語表現
flat	同格複数単語表現
goeswith	1単語分割表現
iobj	関節目的語
list	リスト表現
mark	接続詞
nmod	名詞修飾語
nsubj	主語名詞
nummod	数詞修飾語
obj	目的語
obl	斜格名詞
orphan	独立関係
parataxis	並列
punct	句読点
reparandum	単語として認識されない単語表現
root	文の根(ROOTは人為的に付加される)
vocative	発声関係
xcomp	補体
```

## 参照

自然言語処理におけるPOSタグと係り受けタグ一覧

https://qiita.com/kei_0324/items/400f639b2f185b39a0cf


# GiNZA Tips

## メモリサイズ

マルチプロセス実行では1プロセスあたりja_ginzaで数百MB、ja_ginza_electraで数GBのメモリが必要です。

https://megagonlabs.github.io/ginza/command_line_tool.html





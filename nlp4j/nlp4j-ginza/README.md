# Introduction 

NLP4J for GiNZA

https://megagonlabs.github.io/ginza/

# How to run GiNZA server

```
python nlp4j-ginza-ginzaserver\ginzaserver.py
```

# Universal Dependency 関連論文

https://www.jstage.jst.go.jp/article/jnlp/26/1/26_3/_pdf

# POS Map

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

# GiNZA Memo


マルチプロセス実行では1プロセスあたりja_ginzaで数百MB、ja_ginza_electraで数GBのメモリが必要です。
https://megagonlabs.github.io/ginza/command_line_tool.html




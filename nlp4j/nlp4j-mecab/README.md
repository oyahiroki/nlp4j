# Introduction 

NLP4J for Mecab Annotator

https://en.wikipedia.org/wiki/MeCab

# インストール on Windows

- Mecab for Windows 64bit を導入

- libmecab.dll を System32 にコピーする (環境変数PATHに「C:\Program Files\MeCab\bin\」を追加してもおそらく同じ効果)

```
COPY FROM:
"C:\Program Files\MeCab\bin\libmecab.dll"

COPY TO:
"C:\Windows\System32\libmecab.dll"
```

# インストール Mecab on Ubuntu

```
$ sudo apt install mecab
$ sudo apt install libmecab-dev
$ sudo apt install mecab-ipadic-utf8
```

mecab-ipadic-neologのインストール

```
$ git clone https://github.com/neologd/mecab-ipadic-neologd.git
$ cd mecab-ipadic-neologd
$ sudo bin/install-mecab-ipadic-neologd
```

辞書を指定して起動

```
$ mecab -d /usr/lib/x86_64-linux-gnu/mecab/dic/mecab-ipadic-neologd
```

デフォルト辞書の変更

```
$ cat /etc/mecabrc
;
; Configuration file of MeCab
;
; $Id: mecabrc.in,v 1.3 2006/05/29 15:36:08 taku-ku Exp $;
;
dicdir = /var/lib/mecab/dic/debian

; userdic = /home/foo/bar/user.dic

; output-format-type = wakati
; input-buffer-size = 8192

; node-format = %m\n
; bos-format = %S\n
; eos-format = EOS\n
```

オプションいろいろ

```
$ pip3 install --upgrade --force-reinstall mecab-python3
$ pip install --user mecab-python3
```

```
$ pip uninstall mecab
Found existing installation: mecab 0.996.3
Uninstalling mecab-0.996.3:
  Would remove:
    /home/oyahiroki/.local/lib/python3.10/site-packages/MeCab.py
    /home/oyahiroki/.local/lib/python3.10/site-packages/_MeCab.cpython-310-x86_64-linux-gnu.so
    /home/oyahiroki/.local/lib/python3.10/site-packages/mecab-0.996.3.dist-info/*
Proceed (Y/n)? Y
  Successfully uninstalled mecab-0.996.3
```







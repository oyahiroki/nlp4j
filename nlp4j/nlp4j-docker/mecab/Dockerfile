
FROM ubuntu:18.04

# basic libs
RUN apt update -y && apt upgrade -y
RUN apt install -y wget build-essential gcc zlib1g-dev

# Japanese
RUN apt update \
    && apt install -y locales \
    && locale-gen ja_JP.UTF-8
ENV LANG ja_JP.UTF-8
ENV LANGUAGE ja_JP:ja
ENV LC_ALL=ja_JP.UTF-8
RUN localedef -f UTF-8 -i ja_JP ja_JP.utf8

# latest openssl for python
WORKDIR /root/
RUN wget https://www.openssl.org/source/openssl-1.1.1d.tar.gz \
        && tar zxf openssl-1.1.1d.tar.gz \
        && cd openssl-1.1.1d \
        && ./config \
        && make \
        && make install

# python
# WORKDIR /root/
# RUN wget https://www.python.org/ftp/python/3.6.8/Python-3.6.8.tgz \
#        && tar zxf Python-3.6.8.tgz \
#        && cd Python-3.6.8 \
#        && ./configure \
#        && make altinstall
#ENV PYTHONIOENCODING "utf-8"

# WORKDIR /usr/local/bin/
# RUN ln -s python3.6 python
# RUN ln -s pip3.6 pip

# mecab
RUN apt install -y mecab libmecab-dev mecab-ipadic mecab-ipadic-utf8

# python app settings
# ADD requirements.txt ./requirements.txt
# RUN pip install -r requirements.txt

WORKDIR /

COPY download/input.txt /root/

RUN echo "OK"



FROM ubuntu:18.04

# basic libs
RUN apt update -y && apt upgrade -y
RUN apt install -y wget build-essential gcc zlib1g-dev

# latest openssl for python
WORKDIR /root/
RUN wget https://www.openssl.org/source/openssl-1.1.1d.tar.gz \
        && tar zxf openssl-1.1.1d.tar.gz \
        && cd openssl-1.1.1d \
        && ./config \
        && make \
        && make install

# Japanese
RUN apt update \
    && apt install -y locales \
    && locale-gen ja_JP.UTF-8
ENV LANG ja_JP.UTF-8
ENV LANGUAGE ja_JP:ja
ENV LC_ALL=ja_JP.UTF-8
RUN localedef -f UTF-8 -i ja_JP ja_JP.utf8

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

# Setup cabocha ...

WORKDIR /root/

COPY download/CRF++-0.58.tar.gz /root/
COPY download/cabocha-0.69.tar.bz2 /root/
COPY download/input.txt /root/

WORKDIR /root/

RUN tar zxvf CRF++-0.58.tar.gz \
        && cd CRF++-0.58 \
        && ./configure \
        && make \
        && make install

WORKDIR /root/

RUN echo "/usr/local/lib" >> /etc/ld.so.conf.d/lib.conf
RUN ldconfig

RUN tar -jxf cabocha-0.69.tar.bz2

WORKDIR /root/cabocha-0.69/

RUN ./configure -with-charset=utf-8

RUN make
RUN make install

WORKDIR /root/

# TO PREVENT ERROR
# cabocha: error while loading shared libraries: libcabocha.so.5: cannot open shared object file: No such file or directory
RUN echo "include /usr/local/lib" >> /etc/ld.so.conf
RUN ldconfig

WORKDIR /

RUN rm -rf /root/CRF++-0.58/
RUN rm -rf /root/cabocha-0.69/
RUN rm -rf /root/openssl-1.1.1d/
RUN rm /root/CRF++-0.58.tar.gz
RUN rm /root/cabocha-0.69.tar.bz2
RUN rm /root/openssl-1.1.1d.tar.gz

RUN echo "OK"


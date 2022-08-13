
FROM ubuntu:18.04

# basic libs
RUN apt update -y && apt upgrade -y

# Japanese
RUN apt update \
    && apt install -y locales \
    && locale-gen ja_JP.UTF-8
ENV LANG ja_JP.UTF-8
ENV LANGUAGE ja_JP:ja
ENV LC_ALL=ja_JP.UTF-8
RUN localedef -f UTF-8 -i ja_JP ja_JP.utf8


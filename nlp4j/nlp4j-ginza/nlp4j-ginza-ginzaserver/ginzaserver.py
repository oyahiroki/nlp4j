# coding: utf-8

from http.server import HTTPServer
from http.server import BaseHTTPRequestHandler
from urllib.parse import urlparse
import urllib.parse
from ginza import *
import spacy
import json
from collections import defaultdict
import traceback

from socketserver import ThreadingMixIn
import threading

class GinzaHttpRequestHandler(BaseHTTPRequestHandler):
    def __init__(self, request, client_address, server):
        print("init request handler")
#        self.nlp = spacy.load("ja_ginza")
        BaseHTTPRequestHandler.__init__(self,request,client_address,server)
    
    def do_GET(self):

        query = urlparse(self.path).query
        print("query=" + query)

        qs_d = urllib.parse.parse_qs(query)
        print(qs_d)
        print(hasattr(qs_d,"text"))
        print("text" in qs_d)
        # check parameter
        if ("text" in qs_d) == False:
            print("hi2")
            self.send_response(404)
            self.end_headers()
            return
        
        text = qs_d["text"][0]
        # decode requested value
        text = urllib.parse.unquote(text)

        try:
            doc = self.nlp(text)
            res = []

            for sent in doc.sents:
                resx = {}
                res.append(resx)
                # print(type(sent.raw))
                # res.append(sent.raw)
                pps = []
                resx["paragraphs"] = pps
                pgps = {}
                pps.append(pgps)
                sts = []
                pgps["sentences"] = sts
                stcs = {}
                sts.append(stcs)
                pgps["raw"] = sent.text
                tokens = []
                stcs["tokens"] = tokens
                # res.append(tokens)
                for token in sent:
                    tk = {}
                    tk["id"] = token.i
                    tk["orth"] = token.orth_
                    tk["tag"] = token.tag_
                    tk["pos"] = token.pos_
                    tk["lemma"] = token.lemma_
                    tk["head"] = token.head.i
                    tk["dep"] = token.dep_
                    tokens.append(tk)
#                     o["paragraphs"].append(tk)
#                     tk["id"] = token
#                     tokens.append(tk)
#                     print(token.i)
#                     print(token.orth_)
#                     print(token.lemma_)
#                     print(token.pos_)
#                     print(token.tag_)
#                     print(token.dep_)
#                     print(token.head.i)
            self.send_response(200)
            self.send_header("Content-type","application/json; charset=utf-8")
            self.end_headers()
            html = json.dumps(res)
            self.wfile.write(html.encode())
            return
        except:
            print(traceback.format_exc())
            self.send_response(500)

class GinzaHttpServer(ThreadingMixIn, HTTPServer):
    def __init__(self, address, handlerClass=GinzaHttpRequestHandler):
        print("init GinzaHttpServer")
        # handlerClass.nlp = spacy.load("ja_ginza") # 従来型モデル
        handlerClass.nlp = spacy.load("ja_ginza_electra") # ja_ginza_electra
        super().__init__(address, handlerClass)

def main():
    ip = '127.0.0.1'
    port = 8888

    server = GinzaHttpServer((ip,port),GinzaHttpRequestHandler)
    try:
        server.serve_forever()
    except KeyboardInterrupt:
        pass
    finally:
        server.server_close()


if __name__ == '__main__':
    main()

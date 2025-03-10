# coding: utf-8

# コサイン類似度専用サーバー
# Cosine Simirality Server

from http.server import HTTPServer
from http.server import BaseHTTPRequestHandler
from urllib.parse import urlparse
import urllib.parse
import json
import traceback
from socketserver import ThreadingMixIn
import threading
import sys
import time
import datetime
import signal
from sentence_transformers import SentenceTransformer, util
import argparse

signal.signal(signal.SIGINT, signal.SIG_DFL)

def sig_handler(signum, frame) -> None:
	sys.exit(1)

# 初期化
print("initializing ... 1/2")
model = SentenceTransformer('intfloat/multilingual-e5-large')
print("initializing ... 2/2")
model.encode(["test"])
# 初期化...終了
print("initializing ... done")


class HelloHttpRequestHandler(BaseHTTPRequestHandler):
	count = 0
	
	def __init__(self, request, client_address, server):
		print("init request handler")
		BaseHTTPRequestHandler.__init__(self,request,client_address,server)
	
	def do_POST(self):
		try:
#            print(self.headers)
			content_length = int(self.headers.get('content-length'))
			requestbodyjson = json.loads(self.rfile.read(content_length).decode('utf-8'))
			# print(requestbodyjson)
			
			response_data = {}
			
			# print(requestbodyjson['s'])
			# s
			response_data['s'] = requestbodyjson['s']
			query_embedding = model.encode(requestbodyjson['s'])

			corpus_embeddings = model.encode(requestbodyjson['tt'])
			r = util.semantic_search(query_embedding, corpus_embeddings)
			# print(r)
			response_data["tt"] = requestbodyjson['tt']
			response_data["r"] = r[0]

			# print(response_data)
			
			# JSON形式でレスポンスを返す
			response_json = json.dumps(response_data).encode('utf-8')

			self.send_response(200)
			self.send_header("Content-type","application/json; charset=utf-8")
			self.end_headers()
			# wfile Contains the output stream for writing a response back to the client. Proper adherence to the HTTP protocol must be used when writing to this stream in order to achieve successful interoperation with HTTP clients.
			self.wfile.write(response_json)
			self.close_connection = True
			# del
			del response_json
			del response_data
		except KeyboardInterrupt:
			print("catch on main")
			raise
		except Exception as e:
			print(e)
			self.send_response(500)
			self.send_header('Content-type','application/json')
			self.end_headers()
			response = {}
			responsebody = json.dumps(response)
			self.wfile.write(responsebody.encode('utf-8'))

	def do_GET(self):
		self.send_response(404)
		self.end_headers()
		return

class HelloHttpServer(ThreadingMixIn, HTTPServer):
	pass
#	def __init__(self, address, handlerClass=HelloHttpRequestHandler, option=0):
#		print("init")
#		print("curl -X POST -H \"Content-Type: application/json\" -d \"{'s': '今日はとてもいい天気です。', 'tt': ['私は学校に行きます。']}\" http://" + address[0] + ":" + str(address[1]) + "/")
#		super().__init__(address, handlerClass)

def main():
	signal.signal(signal.SIGTERM, sig_handler)
#    ip = '127.0.0.1'
	ip = '0.0.0.0'
	
	parser = argparse.ArgumentParser(description="nlp4j-embedding")
	parser.add_argument("-p", "--port", type=int, default=8888, help="Port Number")
	args = parser.parse_args()	
	
	port = args.port

	server = HelloHttpServer((ip,port),HelloHttpRequestHandler)
	
	try:
		server.serve_forever()
	except KeyboardInterrupt:
		pass
	finally:
		server.server_close()

if __name__ == '__main__':
	main()


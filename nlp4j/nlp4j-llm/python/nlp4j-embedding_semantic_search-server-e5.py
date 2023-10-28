# coding: utf-8

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

signal.signal(signal.SIGINT, signal.SIG_DFL)

def sig_handler(signum, frame) -> None:
	sys.exit(1)

class HelloHttpRequestHandler(BaseHTTPRequestHandler):

	count = 0
	model = SentenceTransformer('intfloat/multilingual-e5-large')
	
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
			query_embedding = self.model.encode(requestbodyjson['s'])

			corpus_embeddings = self.model.encode(requestbodyjson['tt'])
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
	def __init__(self, address, handlerClass=HelloHttpRequestHandler, option=0):
		print("init")
		print("curl -X POST -H \"Content-Type: application/json\" -d \"{'s': '今日はとてもいい天気です。', 'tt': ['私は学校に行きます。']}\" http://" + address[0] + ":" + str(address[1]) + "/")
		super().__init__(address, handlerClass)

def main():
	
	signal.signal(signal.SIGTERM, sig_handler)
	ip = '127.0.0.1'
	port = 8888
	args = sys.argv[1:]
	if (len(args)==1):
		port = int(args[0])
	server = HelloHttpServer((ip,port),HelloHttpRequestHandler)
	try:
		server.serve_forever()
	except KeyboardInterrupt:
		pass
	finally:
		server.server_close()

if __name__ == '__main__':
	main()

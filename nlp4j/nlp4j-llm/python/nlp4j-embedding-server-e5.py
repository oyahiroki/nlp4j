# coding: utf-8

from http.server import HTTPServer
from http.server import BaseHTTPRequestHandler
from urllib.parse import urlparse
import urllib.parse
import json
from collections import defaultdict
import traceback

from socketserver import ThreadingMixIn
import threading

import sys
import time

import datetime

import signal

from sentence_transformers import SentenceTransformer

signal.signal(signal.SIGINT, signal.SIG_DFL)


def sig_handler(signum, frame) -> None:
	sys.exit(1)

class HelloHttpRequestHandler(BaseHTTPRequestHandler):

	count = 0
	
	def __init__(self, request, client_address, server):
		self.model = SentenceTransformer('intfloat/multilingual-e5-large')
		BaseHTTPRequestHandler.__init__(self,request,client_address,server)
	
	def log_message(self, format, *args):
		pass
	
	def myProcess(self, text):
		HelloHttpRequestHandler.count += 1
		try:
			time1 = time.time()
			# doc = self.nlp(text)
			res = {}
			res["message"] = "ok"
			res["time"] = datetime.datetime.now().strftime("%Y-%m-%dT%H:%M:%S")
			res["text"] = text
			sentences = [text]
			embeddings = self.model.encode(sentences)
			# print(embeddings)
			# print("---")
			# print(embeddings.tolist())
			res["embeddings"] = embeddings.tolist()[0]
			for sentence, embedding in zip(sentences, embeddings):
				print("Sentence:", sentence)
				print("Embedding:", embedding)
				print("")

			self.send_response(200)
			self.send_header("Content-type","application/json; charset=utf-8")
			self.end_headers()
			html = json.dumps(res)
			# wfile Contains the output stream for writing a response back to the client. Proper adherence to the HTTP protocol must be used when writing to this stream in order to achieve successful interoperation with HTTP clients.
			self.wfile.write(html.encode())
			self.close_connection = True
			time2 = time.time()
			# print( datetime.datetime.now().strftime("%Y-%m-%dT%H:%M:%S") + ", time=" + str( (time2 - time1) * 1000 ) + ", text.length=" + str(len(text)) + ", text[0:8]=" + text[0:8])
			# print( datetime.datetime.now().strftime("%Y-%m-%dT%H:%M:%S") + "," + "{:9}".format(GinzaHttpRequestHandler.count) + ", time=" + "{:6.2f}".format( (time2 - time1) * 1000 ) + ", text.length=" + str(len(text)) + ", text[0:8]=" + text[0:8])
			del html
			del res
			# del doc
			del time2
			del time1
			return
		except KeyboardInterrupt:
			print("catch on main")
			raise
		except:
			print(traceback.format_exc())
			self.send_response(500)

	def do_POST(self):
		try:
#            print(self.headers)
			content_length = int(self.headers.get('content-length'))
			requestbody = json.loads(self.rfile.read(content_length).decode('utf-8'))
			text = requestbody.get('text')
			self.myProcess(text)
#            print('text: ' + text )
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
		query = urlparse(self.path).query
#        print("query=" + query)

		qs_d = urllib.parse.parse_qs(query)
		# print(qs_d)
		# print('has attribute text:' + str(hasattr(qs_d,"text")))
		# print("text" in qs_d)
		# check parameter
		if ("text" in qs_d) == False:
			self.send_response(404)
			self.end_headers()
			return
		
		text = qs_d["text"][0]
		# decode requested value
		text = urllib.parse.unquote(text)
		self.myProcess(text)

class HelloHttpServer(ThreadingMixIn, HTTPServer):
	def __init__(self, address, handlerClass=HelloHttpRequestHandler, option=0):
		print("init HttpServer")
		# http://127.0.0.1:8888/?text=%E3%81%93%E3%82%8C%E3%81%AF%E3%83%86%E3%82%B9%E3%83%88%E3%81%A7%E3%81%99%E3%80%82
		print("http://" + address[0] + ":" + str(address[1]) + "/?text=これはテストです。")
		print("http://" + address[0] + ":" + str(address[1]) + "/?text=%E3%81%93%E3%82%8C%E3%81%AF%E3%83%86%E3%82%B9%E3%83%88%E3%81%A7%E3%81%99%E3%80%82")
		print("curl -X POST -H \"Content-Type: application/json\" -d \"{\\\"text\\\":\\\"これはテストです。\\\"}\" http://" + address[0] + ":" + str(address[1]) + "/")
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


import urllib.request
import json

def main():
    url = 'http://localhost:8888'
    method = 'POST'
    headers = {'Content-Type' : 'application/json'}

    obj = {'text' : '今日はいい天気です'}
    requestbody = json.dumps(obj).encode('utf-8')

    request = urllib.request.Request(url,data=requestbody,method=method,headers=headers)
    with urllib.request.urlopen(request) as response:
        response_body = response.read().decode('utf-8')
#        print(response_body)
        response = json.loads(response_body)
#        print(response)
        print(json.dumps(response,indent=2,ensure_ascii=False))

if __name__ == '__main__':
    main()

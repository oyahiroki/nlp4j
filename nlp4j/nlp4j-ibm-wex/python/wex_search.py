import requests
import json
from typing import List

class WexSearchClient:
    def __init__(self, collection="sandbox", language="en", locale="en", endpoint="http://localhost:8393"):
        self.collection = collection
        self.language = language
        self.locale = locale
        self.end_point = endpoint

    def search(self, query: str, start: int, results: int) -> dict:
        url = f"{self.end_point}/api/v10/search"
        params = {
            "collection": self.collection,
            "output": "application/json",
            "enableHref": "false",
            "query": query,
            "queryLang": self.language,
            "linguistic": "engine",
            "synonymExpansion": "automatic",
            "nearDuplication": "shingle",
            "documentPart": "aggregation",
            "summaryLengthRatio": "100",
            "sortKey": "relevance",
            "sortOrder": "desc",
            "start": start,
            "results": results
        }
        response = requests.get(url, params=params)
        return response.json()

    def search_documents(self, query: str, start: int, results: int) -> List[dict]:
        docs = []
        jo = self.search(query, start, results)
        es_result = jo['es_apiResponse']['es_result']

        for jo_es_result in es_result:
            es_relevance = jo_es_result['es_relevance']
            es_summary = jo_es_result['es_summary']
            doc = {
                'text': es_summary,
                'relevance': es_relevance
            }
            docs.append(doc)

        return docs

# テスト
if __name__ == "__main__":
    wex = WexSearchClient(collection="sandbox",language="ja",locale="jp", endpoint="http://192.168.1.23:8393")
    query = "タイヤ"
    start = 0
    results = 10
    docs = wex.search_documents(query, start, results)
    for doc in docs:
        print(doc)

import requests
import json
from collections import defaultdict

class Keyword:
    def __init__(self, facet, upos, lex, string, begin, end):
        self.facet = facet
        self.upos = upos
        self.lex = lex
        self.string = string
        self.begin = begin
        self.end = end

class Document:
    def __init__(self):
        self.attributes = defaultdict(str)
        self.keywords = []

    def get_attribute_as_string(self, target):
        return self.attributes[target]

    def add_keyword(self, keyword):
        self.keywords.append(keyword)

class WexKeywordAnnotator:
    def __init__(self, collection="sandbox", language="en_US", locale="en", endpoint="http://localhost:8393"):
        self.collection = collection
        self.language = language
        self.locale = locale
        self.endpoint = endpoint

    def annotate(self, doc):
        url = f"{self.endpoint}/api/v10/analysis/text"

        if hasattr(doc, 'targets'):
            for target in doc.targets:
                text = doc.get_attribute_as_string(target)

                params = {
                    "language": self.language,
                    "locale": self.locale,
                    "output": "application/json",
                    "collection": self.collection,
                    "text": text
                }

                try:
                    response = requests.get(url, params=params)
                    response.raise_for_status()

                    response_body = response.text
                    jo = json.loads(response_body)

                    content = jo.get("content")

                    textfacets = jo.get("metadata", {}).get("textfacets", [])

                    for kw in textfacets:
                        path = '.'.join(kw.get("path", []))
                        lex = kw.get("keyword")
                        begin = kw.get("begin")
                        end = kw.get("end")
                        string = content[begin:end]

                        upos = None
                        if path == "_word.noun.general":
                            upos = "NOUN"
                        elif path == "_word.adj":
                            upos = "ADJ"
                        elif path == "_word.verb":
                            upos = "VERB"

                        if upos:
                            keyword = Keyword(facet=upos, upos=upos, lex=lex, string=string, begin=begin, end=end)
                            doc.add_keyword(keyword)

                except requests.exceptions.RequestException as e:
                    print(f"Request failed: {e}")

def main():
    # Example usage
    doc = Document()
    doc.targets = ["example_text"]
    doc.attributes["example_text"] = "今日はいい天気です。"
    
    annotator = WexKeywordAnnotator(endpoint="http://192.168.1.23:8393",collection="sandbox", language="ja_JP", locale="ja")
    # annotator = WexKeywordAnnotator()
    annotator.annotate(doc)

    # Output the annotated keywords
    for keyword in doc.keywords:
        print(vars(keyword))

if __name__ == "__main__":
    main()

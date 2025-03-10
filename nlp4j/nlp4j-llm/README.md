
# Javaから Embedding を使用する

1. nlp4j-embedding-server-e5.py を起動する

```
$ python3 nlp4j-embedding-server-e5.py
init HttpServer
http://127.0.0.1:8888/?text=これはテストです。
http://127.0.0.1:8888/?text=%E3%81%93%E3%82%8C%E3%81%AF%E3%83%86%E3%82%B9%E3%83%88%E3%81%A7%E3%81%99%E3%80%82
curl -X POST -H "Content-Type: application/json" -d "{\"text\":\"これはテストです。\"}" http://127.0.0.1:8888/
Expected response: {"message": "ok", "time": "2024-05-26T23:21:38", "text": "これはテストです。", "embeddings": [0.04231283441185951, -0.0035561583936214447, -0.014567600563168526, ... 0.022928446531295776]}

```
2. nlp4j.llm.embeddings.EmbeddingServiceViaHttp を利用する

# Javaから Embedding による Cosine類似度を計算する

1. nlp4j-embedding_cosine_similarity-server-e5.py を起動する

```
$ python3 nlp4j-embedding_cosine_similarity-server-e5.py
```

2. nlp4j.llm.embeddings.SemanticSearchServiceViaHttp を利用する

# Build for Release

	mvn -DperformRelease=true clean deploy

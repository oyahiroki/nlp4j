# ベクトル検索のスコア計算

## 概要

[`LuceneIndex_HelloMain3.java`](src/test/java/nlp4j/lucene9/LuceneIndex_HelloMain3.java)で実装されているベクトル検索のスコアは、**コサイン類似度（Cosine Similarity）**を使用して計算されます。

## スコア計算方法

### 1. デフォルトの類似度関数

[`FieldTypeDef.java`](src/main/java/nlp4j/lucene9/FieldTypeDef.java:26)で定義されているように、デフォルトの類似度関数は`VectorSimilarityFunction.COSINE`です。

```java
private VectorSimilarityFunction vectorSimilarityFunction = VectorSimilarityFunction.COSINE;
```

### 2. コサイン類似度の計算式

コサイン類似度は、2つのベクトル間の角度の余弦値を計算します：

```
cosine_similarity(A, B) = (A · B) / (||A|| × ||B||)
```

ここで：
- `A · B` = ベクトルAとBの内積
- `||A||` = ベクトルAのノルム（長さ）
- `||B||` = ベクトルBのノルム（長さ）

### 3. スコアの範囲

- **最大値**: 1.0（完全に同じ方向）
- **最小値**: -1.0（完全に反対方向）
- **直交**: 0.0（90度の角度）

### 4. Luceneでのスコア変換

Luceneは内部的にコサイン類似度を`[0, 1]`の範囲に正規化します：

```
score = (1 + cosine_similarity) / 2
```

これにより：
- コサイン類似度 1.0 → スコア 1.0
- コサイン類似度 0.0 → スコア 0.5
- コサイン類似度 -1.0 → スコア 0.0

## サンプルコードでの例

### ドキュメントのベクトル

```java
// Document 1
.putVector("vector", new float[] { 1.0f, 0.0f })

// Document 2
.putVector("vector", new float[] { 0.0f, 1.0f })
```

### クエリベクトルとスコア

#### Example 1: クエリベクトル [1.0, 0.0]

```json
"query_vector": [1.0, 0.0]
```

**計算結果**:
- Document 1: `cosine([1.0, 0.0], [1.0, 0.0]) = 1.0` → **スコア 1.0**
- Document 2: `cosine([1.0, 0.0], [0.0, 1.0]) = 0.0` → **スコア 0.5**

#### Example 3: クエリベクトル [0.5, 0.5]

```json
"query_vector": [0.5, 0.5]
```

**計算結果**:
- Document 1: `cosine([0.5, 0.5], [1.0, 0.0]) = 0.707` → **スコア 0.854**
- Document 2: `cosine([0.5, 0.5], [0.0, 1.0]) = 0.707` → **スコア 0.854**

## 他の類似度関数

Luceneは以下の類似度関数もサポートしています：

### 1. EUCLIDEAN（ユークリッド距離）

```java
schema.add("vector", FieldTypeDef.knnVector(2)
    .similarity(VectorSimilarityFunction.EUCLIDEAN));
```

- 2つのベクトル間の直線距離を計算
- スコア = `1 / (1 + distance)`
- 距離が小さいほどスコアが高い

### 2. DOT_PRODUCT（内積）

```java
schema.add("vector", FieldTypeDef.knnVector(2)
    .similarity(VectorSimilarityFunction.DOT_PRODUCT));
```

- ベクトルの内積を直接使用
- 正規化されたベクトルの場合、コサイン類似度と同じ
- スコア = `(1 + dot_product) / 2`

### 3. MAXIMUM_INNER_PRODUCT

```java
schema.add("vector", FieldTypeDef.knnVector(2)
    .similarity(VectorSimilarityFunction.MAXIMUM_INNER_PRODUCT));
```

- 内積の最大化を目的とした類似度
- 負の値を許容
- スコア = `dot_product` (変換なし)

## 実装の詳細

### スキーマ定義での指定

```java
SearchSchema schema = new SearchSchema();
schema.add("vector", FieldTypeDef.knnVector(2)
    .similarity(VectorSimilarityFunction.COSINE));  // デフォルト
```

### Luceneクエリでの使用

[`LuceneQueryBuilder.java`](src/main/java/nlp4j/lucene9/LuceneQueryBuilder.java)では、スキーマで定義された類似度関数が自動的に使用されます：

```java
// フィルタなし
return KnnFloatVectorField.newVectorQuery(field, queryVector, k);

// フィルタあり
return new KnnFloatVectorQuery(field, queryVector, k, filterQuery);
```

## スコアの解釈

### コサイン類似度の場合

| スコア範囲 | 意味 |
|-----------|------|
| 0.9 - 1.0 | 非常に類似（ほぼ同じ方向） |
| 0.7 - 0.9 | 類似 |
| 0.5 - 0.7 | やや類似 |
| 0.3 - 0.5 | あまり類似していない |
| 0.0 - 0.3 | 類似していない（反対方向） |

## 参考資料

- [Apache Lucene Vector Search Documentation](https://lucene.apache.org/core/9_0_0/core/org/apache/lucene/search/KnnFloatVectorQuery.html)
- [VectorSimilarityFunction API](https://lucene.apache.org/core/9_0_0/core/org/apache/lucene/index/VectorSimilarityFunction.html)
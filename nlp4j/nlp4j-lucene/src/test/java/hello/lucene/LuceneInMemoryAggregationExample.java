package hello.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.LabelAndValue;
import org.apache.lucene.facet.sortedset.DefaultSortedSetDocValuesReaderState;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesFacetCounts;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesFacetField;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesReaderState;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

public class LuceneInMemoryAggregationExample {

	private static final String FIELD_TITLE = "title";
	private static final String FIELD_BODY = "body";
	private static final String FIELD_KEYWORD = "keyword";

	public static void main(String[] args) throws Exception {

		Analyzer analyzer = new WhitespaceAnalyzer();

		Directory directory = new ByteBuffersDirectory();

		FacetsConfig facetsConfig = new FacetsConfig();
		facetsConfig.setMultiValued(FIELD_KEYWORD, true);

		try (IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(analyzer))) {

			addDoc(indexWriter, facetsConfig, "1", "東京", "東京 は 日本 の 首都 です", new String[] { "日本", "都市", "首都" });

			addDoc(indexWriter, facetsConfig, "2", "大阪", "大阪 は 日本 の 都市 です", new String[] { "日本", "都市", "関西" });

			addDoc(indexWriter, facetsConfig, "3", "京都", "京都 は 古都 で 観光地 です", new String[] { "日本", "都市", "観光", "関西" });

			addDoc(indexWriter, facetsConfig, "4", "Java", "Java は プログラミング 言語 です",
					new String[] { "IT", "Java", "プログラミング" });

			indexWriter.commit();
		}

		try (DirectoryReader reader = DirectoryReader.open(directory)) {

			IndexSearcher searcher = new IndexSearcher(reader);

			// Lucene QueryParser 形式の検索条件
			// 例:
			// body:日本
			// body:日本 AND keyword_search:都市
			// title:東京 OR title:大阪
			//
			// ここでは body フィールドをデフォルト検索対象にします。
			QueryParser parser = new QueryParser(FIELD_BODY, analyzer);

			searchAndAggregate(searcher, reader, facetsConfig, parser, "日本");
			System.out.println("---");
			searchAndAggregate(searcher, reader, facetsConfig, parser, "日本 AND 都市");
			System.out.println("---");
			searchAndAggregate(searcher, reader, facetsConfig, parser, "プログラミング");
			System.out.println("---");
			searchAndAggregate(searcher, reader, facetsConfig, parser, "keyword_search:Java");
			System.out.println("---");
			searchAndAggregate(searcher, reader, facetsConfig, parser, "*:*");
		}
	}

	private static void addDoc(IndexWriter writer, FacetsConfig facetsConfig, String id, String title, String body,
			String[] keywords) throws IOException {

		Document doc = new Document();
		{
			doc.add(new StringField("id", id, Store.YES));
			doc.add(new TextField(FIELD_TITLE, title, Store.YES));
			doc.add(new TextField(FIELD_BODY, body, Store.YES));
		}

		for (String keyword : keywords) {
			// aggregation 用
			doc.add(new SortedSetDocValuesFacetField(FIELD_KEYWORD, keyword));

			// 検索条件でも keyword を使いたい場合用
			// QueryParser で keyword_search:都市 のように検索できる
			doc.add(new StringField("keyword_search", keyword, Store.YES));
		}

		writer.addDocument(facetsConfig.build(doc));
	}

	private static void searchAndAggregate(IndexSearcher searcher, DirectoryReader reader, FacetsConfig facetsConfig,
			QueryParser parser, String luceneQueryString) throws Exception {

		Query query = parser.parse(luceneQueryString);

		// 1. 上位10件の検索結果
		TopDocs topDocs = searcher.search(query, 10);

		// 2. facet/aggregation 用に一致docを収集
		FacetsCollector facetsCollector = new FacetsCollector();
		searcher.search(query, facetsCollector);

		// 3. SortedSetDocValues facet の reader state
		SortedSetDocValuesReaderState state = new DefaultSortedSetDocValuesReaderState(reader, facetsConfig);

		// 4. aggregation
		SortedSetDocValuesFacetCounts facets = new SortedSetDocValuesFacetCounts(state, facetsCollector);

		FacetResult result = facets.getTopChildren(10, FIELD_KEYWORD);

		System.out.println();
		System.out.println("query = " + luceneQueryString);
		System.out.println("hit count = " + topDocs.totalHits.value);

		if (result == null) {
			System.out.println("keyword aggregation: no result");
			return;
		}

		System.out.println("keyword aggregation:");
		for (LabelAndValue lv : result.labelValues) {
			System.out.println("  " + lv.label + " = " + lv.value);
		}
	}
}
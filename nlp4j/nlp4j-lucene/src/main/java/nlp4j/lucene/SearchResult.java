package nlp4j.lucene;

public class SearchResult {
	public String id;
	public String body;
	public float score;

	/**
	 * addJson() でドキュメントを追加した場合に設定される元の JSON 文字列。
	 * add(id, body) で追加した場合は null。
	 */
	public String data;
}

package nlp4j;

import java.util.List;

/**
 * 自然言語処理の結果. <br/>
 * NLP Response.
 * 
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public interface NlpServiceResponse {

	/**
	 * 自然言語処理サービスが返したレスポンスを返します。
	 * 
	 * @return 自然言語処理サービスが返したレスポンス
	 * @since 1.0
	 */
	public String getOriginalResponseBody();

	/**
	 * 自然言語処理サービスが返したレスポンスコードを返します。
	 * 
	 * @return 自然言語処理サービスが返したレスポンスコード
	 * @since 1.0
	 */
	public int getResponseCode();

	/**
	 * 自然言語処理サービスが返したキーワードを返します。
	 * 
	 * @return 自然言語処理サービスが返したキーワード
	 * @since 1.0
	 */
	public List<Keyword> getKeywords();

}

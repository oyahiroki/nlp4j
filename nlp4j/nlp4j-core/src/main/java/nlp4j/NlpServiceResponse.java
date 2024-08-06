package nlp4j;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 自然言語処理の結果. <br>
 * NLP Response.
 * 
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public interface NlpServiceResponse extends Response {

	/**
	 * @return response body as json element
	 * @since 1.3.7.13
	 */
	public JsonElement getAsJson();

	/**
	 * created on: 2023-07-22
	 * 
	 * @return response as JsonObject
	 * @since 1.3.7.9
	 */
	public JsonObject getAsJsonObject();

	/**
	 * @return response headers as String
	 * @since 1.3.7.13
	 */
	public String getHeaders();

	/**
	 * 自然言語処理サービスが返したキーワードを返します。
	 * 
	 * @return 自然言語処理サービスが返したキーワード
	 * @since 1.0
	 */
	public List<Keyword> getKeywords();

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
	 * @return response code is 200 or not
	 * @since 1.3.7.12 (2023-11-07)
	 */
	public boolean ok();

	/**
	 * @param kwds Keywords
	 * @since 1.3.7.6 (2023-02-16)
	 */
	public void setKeywords(List<Keyword> kwds);
}

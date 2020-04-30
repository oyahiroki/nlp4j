package nlp4j;

/**
 * @author Hiroki Oya
 * @since 1.3
 *
 */
public interface Response {

	/**
	 * サービスが返したレスポンスを返します。
	 * 
	 * @return サービスが返したレスポンス
	 * @since 1.3
	 */
	public String getOriginalResponseBody();

	/**
	 * サービスが返したレスポンスコードを返します。
	 * 
	 * @return サービスが返したレスポンスコード
	 * @since 1.3
	 */
	public int getResponseCode();

	public String getMessage();

}

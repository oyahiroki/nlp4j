package nlp4j.cotoha;

/**
 * COTOHA API アクセストークン
 * 
 * @author Hiroki Oya
 * @since 1.0.0.0
 */
public class AccessToken {

	String access_token;
	String token_type;
	long expires_in;
	String scope;
	long issured_at;

	/**
	 * @param access_token アクセストークン
	 * @param token_type   bearer
	 * @param expires_in   残り有効期限(秒)
	 * @param scope        ""
	 * @param issured_at   トークン発行日時(エポックタイムからの経過ミリ秒数)
	 * @see "https://api.ce-cotoha.com/contents/reference/accesstoken.html"
	 */
	public AccessToken(String access_token, String token_type, long expires_in, String scope, long issured_at) {
		super();
		this.access_token = access_token;
		this.token_type = token_type;
		this.expires_in = expires_in;
		this.scope = scope;
		this.issured_at = issured_at;
	}

	/**
	 * @return アクセストークン
	 */
	public String getAccess_token() {
		return access_token;
	}

	/**
	 * @return bearer
	 */
	public String getToken_type() {
		return token_type;
	}

	/**
	 * @return 残り有効期限(エポックタイムからの経過ミリ秒数)
	 */
	public long getExpires_at() {
		return expires_in * 1000 + issured_at;
	}

	/**
	 * @return トークン発行日時(エポックタイムからの経過ミリ秒数)
	 */
	public long getIssured_at() {
		return issured_at;
	}

}

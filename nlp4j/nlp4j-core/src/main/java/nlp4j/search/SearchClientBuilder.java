package nlp4j.search;

public abstract class SearchClientBuilder<B extends SearchClientBuilder<B>> {

	private int connectionTimeoutMillis;
	private int socketTimeoutMillis;

	public abstract B getThis();

	/**
	 * Tells {@link Builder} that created clients should obey the following timeout
	 * when connecting to Solr servers.
	 * <p>
	 * For valid values see
	 * {@link org.apache.http.client.config.RequestConfig#getConnectTimeout()}
	 * </p>
	 */
	public B withConnectionTimeout(int connectionTimeoutMillis) {
		if (connectionTimeoutMillis < 0) {
			throw new IllegalArgumentException("connectionTimeoutMillis must be a non-negative integer.");
		}

		this.connectionTimeoutMillis = connectionTimeoutMillis;
		return getThis();
	}

	/**
	 * Tells {@link Builder} that created clients should set the following read
	 * timeout on all sockets.
	 * <p>
	 * For valid values see
	 * {@link org.apache.http.client.config.RequestConfig#getSocketTimeout()}
	 * </p>
	 */
	public B withSocketTimeout(int socketTimeoutMillis) {
		if (socketTimeoutMillis < 0) {
			throw new IllegalArgumentException("socketTimeoutMillis must be a non-negative integer.");
		}

		this.socketTimeoutMillis = socketTimeoutMillis;
		return getThis();
	}

}

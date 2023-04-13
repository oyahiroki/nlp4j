package nlp4j.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import nlp4j.NlpServiceResponse;
import nlp4j.impl.DefaultNlpServiceResponse;

/**
 * @author Hiroki Oya
 * @since 1.3.7.8
 */
public interface HttpClient extends AutoCloseable {

	/**
	 * created on 2020-04-29
	 * 
	 * @param url APIのURL
	 * @return NLPの結果
	 * @throws IOException 例外発生時にスローされる
	 * @since 1.3.1.0
	 */
	NlpServiceResponse get(String url) throws IOException;

	/**
	 * @param url    APIのURL
	 * @param params API パラメータ
	 * @return NLPの結果
	 * @throws IOException 例外発生時にスローされる
	 */
	NlpServiceResponse get(String url, Map<String, String> params) throws IOException;

	long getContentLength();

	InputStream getInputStream(String url, Map<String, String> params) throws IOException;

	/**
	 * @param url             APIのURL
	 * @param requestHeader   リクエストヘッダ
	 * @param jsonRequestBody リクエストボディJSON
	 * @return NLPの結果
	 * @throws IOException 例外発生時にスローされる
	 * @since 1.3
	 */
	NlpServiceResponse post(String url, Map<String, String> requestHeader, String jsonRequestBody) throws IOException;

	/**
	 * @param url  APIのURL
	 * @param json API パラメータ
	 * @return NLPの結果
	 * @throws IOException 例外発生時にスローされる
	 */
	NlpServiceResponse post(String url, String json) throws IOException;

}
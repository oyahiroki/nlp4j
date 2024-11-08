package nlp4j.http;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.google.gson.JsonObject;

import nlp4j.NlpServiceResponse;

/**
 * @author Hiroki Oya
 * @since 1.3.7.8
 */
public interface HttpClient extends Closeable {
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

	NlpServiceResponse get(String url, Map<String, String> headers, Map<String, String> params) throws IOException;

	/**
	 * GET
	 * 
	 * @param url             APIのURL
	 * @param requestHeader   リクエストヘッダ
	 * @param jsonRequestBody リクエストボディJSON
	 * @return NLPの結果
	 * @throws IOException 例外発生時にスローされる
	 * @since 1.3.7.14
	 */
	NlpServiceResponse get(String url, Map<String, String> requestHeader, String jsonRequestBody) throws IOException;

	/**
	 * @param url
	 * @param jsonBody
	 * @return
	 * @throws IOException
	 * @since 1.3.7.14
	 */
	NlpServiceResponse get(String url, String jsonBody) throws IOException;

	/**
	 * @return Content length of InputStream
	 */
	long getContentLength();

	/**
	 * GET as InputStream
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	InputStream getInputStream(String url, Map<String, String> params) throws IOException;

	/**
	 * created on 2024-08-08
	 * 
	 * @since 1.3.7.13
	 * 
	 * @param url
	 * @param params
	 * @param requestHeader
	 * @param requestBody
	 * @return
	 * @throws IOException
	 */
	InputStream getInputStreamPost(String url, Map<String, String> params, Map<String, String> requestHeader,
			JsonObject requestBody) throws IOException;

	/**
	 * POST
	 * 
	 * @param url             APIのURL
	 * @param requestHeader   リクエストヘッダ
	 * @param jsonRequestBody リクエストボディJSON
	 * @return NLPの結果
	 * @throws IOException 例外発生時にスローされる
	 * @since 1.3
	 */
	NlpServiceResponse post(String url, Map<String, String> requestHeader, String jsonRequestBody) throws IOException;

	/**
	 * @param url
	 * @param requestParams フォームリクエストパラメータ
	 * @return
	 * @throws IOException
	 * @since 1.3.7.15
	 */
	NlpServiceResponse post(String url, Map<String, String> requestParams) throws IOException;

	/**
	 * POST
	 * 
	 * @param url  APIのURL
	 * @param json API パラメータ
	 * @return NLPの結果
	 * @throws IOException 例外発生時にスローされる
	 */
	NlpServiceResponse post(String url, String json) throws IOException;

}
package nlp4j.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpHead;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;

/**
 * 
 * @since 1.3.7.15
 */
public class HttpUtils {

	/**
	 * 指定された URL にアクセスし、HTTP ステータスコードが 200 (OK) であるかどうかを判定します。
	 * <p>
	 * まず HEAD リクエストを送信し、サーバーが HEAD メソッドをサポートしていない場合 （HTTP 405 または 501 を返す場合）には GET
	 * リクエストで再試行します。 例外が発生した場合やステータスコードが 200 以外の場合は false を返します。
	 * </p>
	 *
	 * Determines whether the given URL responds with HTTP status code 200 (OK).
	 * <p>
	 * This method first tries a HEAD request. If the server does not support the
	 * HEAD method (i.e., returns HTTP 405 or 501), it falls back to a GET request.
	 * Returns false if an exception occurs or if the status code is not 200.
	 * </p>
	 *
	 * @param url チェック対象の URL The URL to be checked
	 * @return ステータスコードが 200 の場合は true、それ以外またはエラー時は false {@code true} if the status
	 *         code is 200, otherwise {@code false}
	 * @since 1.3.7.19
	 */
	public static boolean isStatus200(String url) {
		// まず HEAD を試し、405/501 の場合のみ GET にフォールバック
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpClientResponseHandler<Boolean> handler = response -> {
				int code = response.getCode();
				if (code == 200) {
					return true;
				}
				if (code == 405 || code == 501) { // HEAD未対応の典型
					return client.execute(new HttpGet(url), r2 -> r2.getCode() == 200);
				}
				return false;
			};
			return client.execute(new HttpHead(url), handler);
		} catch (Exception e) {
			return false; // 例外時は false 扱い
		}
	}

	/**
	 * <pre>
	 * HTML Example:
	 * &lt;img src="data:image/jpeg;base64,iVBORw0KGgoAAAANSUhEUgAAA..." alt="Image"&gt;
	 * </pre>
	 * 
	 * @param url
	 * @return Pair of <mimeType, base64data>
	 * @throws IOException : url response is not image
	 * @since 1.3.7.15
	 */
	static public Base64Response fetchImageAsBase64(String url) throws IOException {
		String mimeType = null;
		String base64Image = null;
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpGet request = new HttpGet(url);
			try ( //
					@SuppressWarnings("deprecation")
					CloseableHttpResponse response = httpClient.execute(request); //
			) {
				// MIMEタイプの取得
				mimeType = response.getEntity().getContentType();
//				System.out.println("MIME Type: " + mimeType);

				if (mimeType.contains("image") == false) {
					throw new IOException("not_image_url: " + mimeType + ", " + url);
				}

				// 画像データの取得とBASE64エンコード
				try (InputStream inputStream = response.getEntity().getContent()) {
					byte[] imageBytes = inputStream.readAllBytes();
					base64Image = Base64.getEncoder().encodeToString(imageBytes);
				}

				// レスポンスの解放
				EntityUtils.consume(response.getEntity());
			}
		} catch (Exception e) {
			throw new IOException(e);
		}
		return new Base64Response(mimeType, base64Image);
	}

}

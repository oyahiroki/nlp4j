package nlp4j.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import nlp4j.tuple.Pair;

/**
 * 
 * @since 1.3.7.15
 */
public class HttpUtils {

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

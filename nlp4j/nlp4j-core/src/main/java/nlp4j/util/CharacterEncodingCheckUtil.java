package nlp4j.util;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * created_on: 2024-06-14
 * 
 * @since 1.3.7.13
 */
public class CharacterEncodingCheckUtil {

	private CharsetEncoder encoder;

	public CharacterEncodingCheckUtil(String encode) {
		// Shift_JISエンコーダーを取得
		Charset shiftJisCharset = Charset.forName("Shift_JIS");
		this.encoder = shiftJisCharset.newEncoder();
	}

	/**
	 * Tells whether or not this encoder can encode the given charactersequence.
	 * 
	 * @param s
	 * @return
	 */
	public boolean canEncode(String s) {
		// 文字がShift_JISでエンコード可能かどうかを確認
		// Tells whether or not this encoder can encode the given charactersequence.
		boolean canEncode = encoder.canEncode(s);
		return canEncode;
	}

}

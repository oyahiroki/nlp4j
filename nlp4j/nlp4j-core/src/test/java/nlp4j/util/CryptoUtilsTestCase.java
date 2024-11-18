package nlp4j.util;

import javax.crypto.SecretKey;

import junit.framework.TestCase;

/**
 * Created on : 2024-11-18
 * 
 * @since 1.3.7.16
 */
public class CryptoUtilsTestCase extends TestCase {

	private void checkall() throws Exception {
		String password = "mySecurePassword";
		// キーの生成
		SecretKey key = CryptoUtils.generateKey();
		// 暗号化
		String encryptedPassword = CryptoUtils.encrypt(password, key);
		System.out.println("Encrypted Password: " + encryptedPassword);
		// 復号化
		String decryptedPassword = CryptoUtils.decrypt(encryptedPassword, key);
		System.out.println("Decrypted Password: " + decryptedPassword);

	}

	public void testEncrypt() throws Exception {
		checkall();
	}

	public void testDecrypt() throws Exception {
		checkall();
	}

	public void testGenerateKey() throws Exception {
		checkall();
	}

	public void testIsCryptoSupported() {
		boolean b = CryptoUtils.isCryptoSupported();
		System.err.println(b);
	}

}

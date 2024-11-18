package nlp4j.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Utility class for cryptographic operations such as encryption, decryption,
 * and key generation. Created on : 2024-11-18
 * 
 * @since 1.3.7.16
 */
public class CryptoUtils {

	/**
	 * Encrypts the given data using the provided secret key.
	 *
	 * @param data the plain text data to be encrypted
	 * @param key  the secret key used for encryption
	 * @return the encrypted data encoded as a Base64 string
	 * @throws Exception if an error occurs during the encryption process
	 */
	public static String encrypt(String data, SecretKey key) throws Exception {
		Cipher cipher = Cipher.getInstance("AES"); // Use AES encryption
		cipher.init(Cipher.ENCRYPT_MODE, key); // Initialize the cipher for encryption
		byte[] encryptedData = cipher.doFinal(data.getBytes()); // Perform encryption
		return Base64.getEncoder().encodeToString(encryptedData); // Encode encrypted data as Base64
	}

	/**
	 * Decrypts the given encrypted data using the provided secret key.
	 *
	 * @param encryptedData the Base64 encoded encrypted data
	 * @param key           the secret key used for decryption
	 * @return the decrypted plain text data
	 * @throws Exception if an error occurs during the decryption process
	 */
	public static String decrypt(String encryptedData, SecretKey key) throws Exception {
		Cipher cipher = Cipher.getInstance("AES"); // Use AES decryption
		cipher.init(Cipher.DECRYPT_MODE, key); // Initialize the cipher for decryption
		byte[] decodedData = Base64.getDecoder().decode(encryptedData); // Decode Base64 encoded data
		return new String(cipher.doFinal(decodedData)); // Perform decryption and return plain text
	}

	/**
	 * Generates a new secret key for AES encryption.
	 *
	 * @return a newly generated AES secret key
	 * @throws Exception if an error occurs during key generation
	 */
	public static SecretKey generateKey() throws Exception {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES"); // Get AES key generator
		keyGen.init(128); // Initialize with a key size of 128 bits
		return keyGen.generateKey(); // Generate and return the secret key
	}

	/**
	 * Checks if cryptographic operations (AES) are supported in the current
	 * environment.
	 *
	 * @return true if AES cipher is available, false otherwise
	 */
	public static boolean isCryptoSupported() {
		try {
			Cipher.getInstance("AES"); // Check if AES cipher is available
			return true; // Cryptographic operations are supported
		} catch (Exception e) {
			return false; // Cryptographic operations are not supported
		}
	}

}

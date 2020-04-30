package nlp4j;

/**
 * @author Hiroki Oya
 * @since 1.3
 *
 */
@SuppressWarnings("serial")
public class InvalidPropertyException extends RuntimeException {

	String msg;

	/**
	 * @param key   of Property
	 * @param value Actual Value
	 */
	public InvalidPropertyException(String key, String value) {
		this.msg = "Invalid property: key=" + key + ",value=" + value;
	}

	@Override
	public String getMessage() {
		return this.msg;
	}

}

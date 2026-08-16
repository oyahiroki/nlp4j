package nlp4j.lucene;

/**
 * Result of validating a Lucene query string.
 */
public class LuceneQueryValidationResult {

	private final boolean valid;
	private final String message;

	private LuceneQueryValidationResult(boolean valid, String message) {
		this.valid = valid;
		this.message = message;
	}

	public static LuceneQueryValidationResult valid() {
		return new LuceneQueryValidationResult(true, null);
	}

	public static LuceneQueryValidationResult invalid(String message) {
		return new LuceneQueryValidationResult(false, message);
	}

	public boolean isValid() {
		return valid;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "LuceneQueryValidationResult [valid=" + valid + ", message=" + message + "]";
	}
}
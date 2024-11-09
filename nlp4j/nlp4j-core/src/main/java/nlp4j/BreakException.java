package nlp4j;

/**
 * 
 * @author Hiroki Oya
 * @since 1.3.7.15
 */
public class BreakException extends Exception {

	private static final long serialVersionUID = 1L;

	public BreakException() {
		super();
	}

	public BreakException(String message) {
		super(message);
	}

	public BreakException(Throwable e) {
		super(e);
	}

}

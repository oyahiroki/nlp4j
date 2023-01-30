package nlp4j.wiki;

/**
 * <pre>
 * Exception handling when dump file reading is interrupted
 * ダンプファイルの読み込みが中断されたときに生成されるException
 * </pre>
 * 
 * @author Hiroki Oya
 *
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

package nlp4j;

/**
 * 実行時例外。<br/>
 * Runtime Exception of NLP.
 * 
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class Nlp4JRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * メッセージを指定して初期化します。
	 * 
	 * @param message 詳細メッセージ
	 */
	public Nlp4JRuntimeException(String message) {
		super(message);
	}

}

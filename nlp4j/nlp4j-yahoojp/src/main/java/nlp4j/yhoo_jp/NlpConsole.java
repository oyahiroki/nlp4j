package nlp4j.yhoo_jp;

import nlp4j.util.NlpCLI;

/**
 * @author Hiroki Oya
 * @created_at 2021-05-07
 * @since 1.3.1.0
 */
public class NlpConsole {
	/**
	 * @param args : no parameters
	 * @throws Exception on ERROR
	 */
	public static void main(String[] args) throws Exception {
		YJpDaAnnotatorV2 ann = new YJpDaAnnotatorV2();
		NlpCLI console = new NlpCLI(ann);
		console.run();
	}
}

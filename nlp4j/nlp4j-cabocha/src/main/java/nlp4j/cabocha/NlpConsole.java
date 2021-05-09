package nlp4j.cabocha;

import nlp4j.util.NlpCLI;

/**
 * @author Hiroki Oya
 * @created_at 2021-05-07
 * @since 1.3.1.0
 * 
 */
public class NlpConsole {
	/**
	 * @param args : no parameters
	 * @throws Exception on ERROR
	 */
	public static void main(String[] args) throws Exception {
		CabochaAnnotator ann = new CabochaAnnotator();
		{
			ann.setProperty("tempDir", "R:/");
			ann.setProperty("encoding", "MS932");
			ann.setProperty("target", "text");
		}
		NlpCLI console = new NlpCLI(ann);
		console.run();
	}
}

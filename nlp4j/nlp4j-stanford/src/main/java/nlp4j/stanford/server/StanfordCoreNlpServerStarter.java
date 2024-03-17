package nlp4j.stanford.server;

public class StanfordCoreNlpServerStarter {

	public static void main(String[] args) throws Exception {
		String[] params = { "-port", "9000", "-timeout", "15000" };
		edu.stanford.nlp.pipeline.StanfordCoreNLPServer.main(params);

	}

}

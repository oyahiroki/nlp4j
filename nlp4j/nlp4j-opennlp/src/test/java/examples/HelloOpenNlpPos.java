package examples;

import java.io.InputStream;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class HelloOpenNlpPos {

	public static void main(String[] args) throws Exception {

		try (InputStream in = HelloOpenNlpPos.class.getResourceAsStream("/opennlp-en-ud-ewt-pos-1.3-2.5.4.bin")) {

			POSModel model = new POSModel(in);

			POSTaggerME tagger = new POSTaggerME(model);

			String[] tokens = { "Dogs", "are", "running", "quickly", "." };

			String[] tags = tagger.tag(tokens);

			for (int i = 0; i < tokens.length; i++) {
				System.out.printf("%s\t%s%n", tokens[i], tags[i]);
			}
		}
	}
}
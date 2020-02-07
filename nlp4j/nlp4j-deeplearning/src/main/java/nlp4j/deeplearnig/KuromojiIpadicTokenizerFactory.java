package nlp4j.deeplearnig;

import java.io.InputStream;

import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

/**
 * Tokenize factory for kuromoji
 * 
 * @author Hiroki Oya
 * @since 1.0.0.0
 *
 */
public class KuromojiIpadicTokenizerFactory implements TokenizerFactory {
	public Tokenizer create(String toTokenize) {
		if (toTokenize == null || toTokenize.isEmpty()) {
			throw new RuntimeException("Unable to proceed; no sentence to tokenize");
		}

		KuromojiIpadicTokenizer ret = new KuromojiIpadicTokenizer(toTokenize);
		return ret;
	}

	public Tokenizer create(InputStream paramInputStream) {
		throw new UnsupportedOperationException();
	}

	public void setTokenPreProcessor(TokenPreProcess preProcess) {
		// do nothing
	}

	public TokenPreProcess getTokenPreProcessor() {
		return null;
	}

}
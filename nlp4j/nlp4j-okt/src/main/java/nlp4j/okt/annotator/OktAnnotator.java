package nlp4j.okt.annotator;

import java.util.List;

import org.openkoreantext.processor.KoreanTokenJava;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.KeywordBuilder;
import nlp4j.UPOS20;
import scala.collection.Seq;

public class OktAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	@Override
	public void annotate(Document doc) throws Exception {
		for (String target : super.targets) {

			String text = doc.getAttributeAsString(target);

			// Normalize
			CharSequence normalized = OpenKoreanTextProcessorJava.normalize(text);
			// Tokenize
			Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(normalized);
			List<KoreanTokenJava> kk = OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens);

			for (KoreanTokenJava k : kk) {
				String stem = k.getStem();
				String lex = (stem.equals("") == false) ? stem : k.getText();
				String pos = k.getPos().name();
				String upos = null;
				{ // I don't like switch :-)
					if ("Noun".equals(pos)) {
						upos = UPOS20.NOUN;
					} else if ("Verb".equals(pos)) {
						upos = UPOS20.VERB;
					} else if ("Adjective".equals(pos)) {
						upos = UPOS20.ADV;
					} else if ("Josa".equals(pos)) {
						upos = UPOS20.ADP;
					} else if ("Punctuation".equals(pos)) {
						upos = UPOS20.PUNCT;
					} else {
						upos = UPOS20.X;
					}

				}
//				System.err.println("pos: " + pos);
				Keyword kwd = (new KeywordBuilder()) //
						.begin(k.getOffset()) //
						.end((k.getOffset() + k.getLength())) //
						.lex(lex) //
						.str(k.getText()) //
						.facet(upos) //
						.upos(upos) //
						.build();
//				System.out.println("begin: " + );
//				System.out.println("end: " + );
//				System.out.println("length: " + k.getLength());
//				System.out.println("lex: " + k.getStem()); // LEX 原型
//				System.out.println("str: " + k.getText()); // STR
//				System.out.println("pos: " + k.getPos().name()); // JOSA 助詞 Noun 名詞 Adjective 形容詞 Verb 動詞 Punctation ピリオド
//				System.out.println("isUnknown: " + k.isUnknown());
//				System.out.println("---");
				doc.addKeyword(kwd);
			}

		}

	}

}

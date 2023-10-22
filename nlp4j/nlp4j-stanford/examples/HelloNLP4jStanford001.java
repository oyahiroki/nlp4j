package examples;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentAnnotatorBuilder;
import nlp4j.DocumentBuilder;
import nlp4j.stanford.StanfordPosAnnotator;

public class HelloNLP4jStanford001 {

	public static void main(String[] args) throws Exception {
		String text = "I eat sushi with chopsticks.";
		DocumentAnnotator ann = (new DocumentAnnotatorBuilder<>(StanfordPosAnnotator.class)).set("target", "text")
				.build();
		Document doc = (new DocumentBuilder()).text(text).build();
		ann.annotate(doc);
		doc.getKeywords().forEach(kwd -> {
			System.out.println(kwd.getBegin() + "," + kwd.getEnd() + "," + kwd.getFacet() + "," + kwd.getLex());
		});
	}
}

// Expected output:
// 0,1,word.PRP,I
// 2,5,word.VBP,eat
// 6,11,word.NN,sushi
// 12,16,word.IN,with
// 17,27,word.NNS,chopstick
// 27,28,word..,.

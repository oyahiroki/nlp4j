package examples;

import nlp4j.Document;
import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultDocument;
import nlp4j.stanford.StanfordPosDependencyAnnotator;

public class HelloNLP4jStanford002Dependency {

	public static void main(String[] args) throws Exception {

		StanfordPosDependencyAnnotator ann = new StanfordPosDependencyAnnotator();
		ann.setProperty("target", "text");

		Document doc = new DefaultDocument();
		doc.putAttribute("text", "I eat sushi with chopsticks.");

		ann.annotate(doc);

		doc.getKeywords().forEach(kwd -> {
			if (kwd instanceof KeywordWithDependency) {
				KeywordWithDependency kd = (KeywordWithDependency) kwd;
				System.out.println(kd.toStringAsXml()); // print as xml
				System.out.println("I: " + kwd.getLex());
				kd.getChildren().forEach(child -> {
					System.out.println("children: " + child.getLex());
				});
			}
		});

	}

}
// Expected output
// <?xml version="1.0" encoding="UTF-8"?>
// <w begin="2" depth="0" end="5" facet="VBP" id="0" lex="eat" relation="root" sequence="0" str="eat">
//     <w begin="0" depth="1" end="1" facet="PRP" id="1" lex="I" relation="nsubj" sequence="1" str="I"/>
//     <w begin="6" depth="1" end="11" facet="NN" id="2" lex="sushi" relation="obj" sequence="2" str="sushi"/>
//     <w begin="17" depth="1" end="27" facet="NNS" id="3" lex="chopstick" relation="obl" sequence="3" str="chopsticks">
//         <w begin="12" depth="2" end="16" facet="IN" id="4" lex="with" relation="case" sequence="4" str="with"/>
//     </w>
//     <w begin="27" depth="1" end="28" facet="." id="5" lex="." relation="punct" sequence="5" str="."/>
// </w>
// I: eat
// children: I
// children: sushi
// children: chopstick
// children: .

package examples;

import nlp4j.lucene.LocalSearch;
import nlp4j.lucene.FieldTypeDef;

public class Example21_FieldInfo {

	public static void main(String[] args) throws Exception {
		try (LocalSearch search = LocalSearch //
				.builder("ja") //
				.field("my_field001", FieldTypeDef.keyword().stored(true).aggregatable(true)) //
				.field("my_field002", FieldTypeDef.keyword().stored(true).aggregatable(false)) //
				.build()) {

			{
				java.util.List<String> fields = search.getFields();
				for (String f : fields) {
					System.out.println(f);
				}
			}
			System.out.println("---");
			{
				java.util.List<String> fields = search.getAggregatableFields();
				for (String f : fields) {
					System.out.println(f);
				}
			}

		}
	}

}
//id
//text
//text_en
//text_ja
//data
//word.noun
//word.adv
//word.sym
//word.verb
//word
//word.adp
//word.num
//word.propn
//word.aux
//word.adj
//my_field001
//my_field002
//---
//word.noun
//word.adv
//word.sym
//word.verb
//word
//word.adp
//word.num
//word.propn
//word.aux
//word.adj
//my_field001

package nlp4j.wordnetja;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * created_at 2021-12-04 12:18
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class Synset {

	String id;

	int baseConcept;

	String definition_gloss;

	String statement_example;

	ArrayList<SynsetRelation> synsetRelations = new ArrayList<>();

	public void addSynsetRelations(SynsetRelation synsetRelation) {
		this.synsetRelations.add(synsetRelation);
	}

	public int getBaseConcept() {
		return baseConcept;
	}

	public String getDefinition_gloss() {
		return definition_gloss;
	}

	public String getId() {
		return id;
	}

	/**
	 * @return Statement Example 例文
	 */
	public String getStatement_example() {
		return statement_example;
	}

	public List<SynsetRelation> getSynsetRelations() {
		return synsetRelations;
	}

	public void setBaseConcept(int baseConcept) {
		this.baseConcept = baseConcept;
	}

	public void setDefinition_gloss(String definition_gloss) {
		this.definition_gloss = definition_gloss;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param statement_example 例文
	 */
	public void setStatement_example(String statement_example) {
		this.statement_example = statement_example;
	}

	@Override
	public String toString() {
		return "Synset [id=" + id + ", baseConcept=" + baseConcept + ", definition_gloss=" + definition_gloss
				+ ", statement_example=" + statement_example + ", synsetRelations=" + synsetRelations + "]";
	}

}

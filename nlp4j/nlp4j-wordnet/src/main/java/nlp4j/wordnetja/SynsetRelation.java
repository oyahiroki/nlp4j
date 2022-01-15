package nlp4j.wordnetja;

/**
 * <pre>
 * created_at 2021-12-04 12:20
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class SynsetRelation {

	String targets;
	String relType;

	public String getRelType() {
		return relType;
	}

	public String getTargets() {
		return targets;
	}

	public void setRelType(String relType) {
		this.relType = relType;
	}

	public void setTargets(String targets) {
		this.targets = targets;
	}

	@Override
	public String toString() {
		return "SynsetRelation [targets=" + targets + ", relType=" + relType + "]";
	}

}

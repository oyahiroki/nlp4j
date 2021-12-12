package nlp4j.wordnetja;

import java.util.ArrayList;
import java.util.List;

public class LexicalEntry {

	String id;
	String lemmaWrittenForm;
	String pos;

	ArrayList<String> synset_ids = new ArrayList<>();

	public void addSynsetId(String synset_id) {
		this.synset_ids.add(synset_id);
	}

	public String getId() {
		return id;
	}

	public String getLemmaWrittenForm() {
		return lemmaWrittenForm;
	}

	public String getPos() {
		return pos;
	}

	public List<String> getSynset_ids() {
		return synset_ids;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLemmaWrittenForm(String lemmaWrittenForm) {
		this.lemmaWrittenForm = lemmaWrittenForm;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	@Override
	public String toString() {
		return "LexicalEntry [id=" + id + ", lemmaWrittenForm=" + lemmaWrittenForm + ", pos=" + pos + ", synset_ids="
				+ synset_ids + "]";
	}

}

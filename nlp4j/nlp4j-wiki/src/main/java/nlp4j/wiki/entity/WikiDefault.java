package nlp4j.wiki.entity;

public class WikiDefault extends AbstractWikiEntity implements WikiEntity {

	public WikiDefault(String text) {
		super();
		this.text = text;
	}

	@Override
	public boolean isCategory() {
		return false;
	}

}

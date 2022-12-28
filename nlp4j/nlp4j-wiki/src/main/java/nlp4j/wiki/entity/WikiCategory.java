package nlp4j.wiki.entity;

public class WikiCategory extends AbstractWikiEntity implements WikiEntity {

	public WikiCategory(String text) {
		super();
		this.text = text;
	}

	@Override
	public boolean isCategory() {
		return true;
	}

}

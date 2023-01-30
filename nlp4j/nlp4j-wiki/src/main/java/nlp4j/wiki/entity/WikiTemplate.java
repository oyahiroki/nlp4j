package nlp4j.wiki.entity;

public class WikiTemplate extends AbstractWikiEntity implements WikiEntity {
	{
		name = "template";
	}

	public WikiTemplate(String text) {
		super();
		this.text = text;
	}

	@Override
	public boolean isCategory() {
		return false;
	}

}

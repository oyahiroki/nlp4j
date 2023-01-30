package nlp4j.wiki.entity;

public interface WikiEntity {

	public String getName();

	public String getText();

	boolean isCategory();

	public boolean isEmpty();

	boolean isTemplate();

}

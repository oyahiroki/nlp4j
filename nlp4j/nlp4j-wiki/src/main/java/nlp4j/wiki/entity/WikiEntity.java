package nlp4j.wiki.entity;

public interface WikiEntity {

	public String getText();

	public boolean isEmpty();

	boolean isTemplate();

	boolean isCategory();

}

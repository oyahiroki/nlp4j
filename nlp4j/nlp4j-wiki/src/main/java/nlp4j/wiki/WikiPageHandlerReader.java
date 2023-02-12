package nlp4j.wiki;

/**
 * <pre>
 * Simple Wiki page reader
 * </pre>
 * 
 * created on 2022-12-09
 * 
 * @author Hiroki Oya
 */
public class WikiPageHandlerReader implements WikiPageHandler {

	private WikiPage page;
	private String id;
	private String title;
	private String text;

	public String getId() {
		return id;
	}

	public WikiPage getPage() {
		return page;
	}

	public String getText() {
		return text;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public void read(WikiPage page) throws BreakException {
		this.page = page;
		this.text = page.getText();
		this.id = page.getId();
		this.title = page.getTitle();
	}

}

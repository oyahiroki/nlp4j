package nlp4j.wiki;

import java.util.List;

import junit.framework.TestCase;
import nlp4j.Keyword;

public class WikiUtilsTestCase extends TestCase {

	public void testExtractSpells() {
//		fail("Not yet implemented");
	}

	public void testNormailzeHeader() {
//		fail("Not yet implemented");
	}

	public void testToHtml001() throws Exception {
		String wikitext = "#なんらかの[[教育]]が体系的に行われる[[組織]]又はその[[設備]]。[[学舎]]、[[まなびや]]。";
		String html = WikiUtils.toHtml(wikitext);
		System.err.println(html);
	}

	public void testToPlainText001() {
		String wikitext = "#なんらかの[[教育]]が体系的に行われる[[組織]]又はその[[設備]]。[[学舎]]、[[まなびや]]。";
		String plaintext = WikiUtils.toPlainText(wikitext);
		System.err.println(wikitext);
		System.err.println(plaintext);

//		<ol>
//		<li>なんらかの<a href="" title="教育">教育</a>が体系的に行われる<a href="" title="組織">組織</a>又はその<a href="" title="設備">設備</a>。<a href="" title="学舎">学舎</a>、<a href="" title="まなびや">まなびや</a>。</li>
//		</ol>

	}

	public void testToPlainText002() {
		String wikitext = "# {{lb|en|US|Canada}} An [[institution]] dedicated to [[teaching]] and [[learning]]; an [[educational]] institution.";
		String plaintext = WikiUtils.toPlainText(wikitext);
		System.err.println(wikitext);
		System.err.println(plaintext);
	}

	// # {{lb|en|US|Canada}} An [[institution]] dedicated to [[teaching]] and
	// [[learning]]; an [[educational]] institution.

	public void testExtractKeywordsFromWikiHtml() {
		String html = //
				"<html><body>"//
						+ "<ol>"//
						+ "<li><a href='gakkou.html' title='学校'>学校xx</a></li>"//
						+ "<li><a href='gakkou.html' title='病院'>病院xx</a></li>"//
						+ "</ol>"//
						+ "</body></html>";
		List<Keyword> kwds = WikiUtils.extractKeywordsFromWikiHtml(html);

		for (Keyword kwd : kwds) {
			System.err.println(kwd.getLex());
		}
	}

}

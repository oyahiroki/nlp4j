package nlp4j.azure.search.importer;

import java.util.Date;

import com.azure.core.util.Configuration;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;

public class AzureSearchDocumentImporterTest extends TestCase {

	private static final String ENDPOINT = Configuration.getGlobalConfiguration()
			.get("AZURE_COGNITIVE_SEARCH_ENDPOINT");
	private static final String ADMIN_KEY = Configuration.getGlobalConfiguration()
			.get("AZURE_COGNITIVE_SEARCH_ADMIN_KEY");

	public void testImportDocument001() throws Exception {

		Document doc = new DefaultDocument();

		doc.putAttribute("id", "2");
		doc.putAttribute("date", new Date());
		doc.putAttribute("text", "Hello, test import.");
		doc.putAttribute("field1", "aaa");
		doc.putAttribute("field2", "bbb");
		doc.putAttribute("field3", "ccc");
		{
			DefaultKeyword kwd = new DefaultKeyword();
			kwd.setFacet("word.NN");
			kwd.setStr("TEST");
			kwd.setLex("test");
			doc.addKeyword(kwd);
		}
		{
			DefaultKeyword kwd = new DefaultKeyword();
			kwd.setFacet("word.NN");
			kwd.setStr("Vehicle");
			kwd.setLex("vehicle");
			doc.addKeyword(kwd);
		}
		{
			DefaultKeyword kwd = new DefaultKeyword();
			kwd.setFacet("word.VB");
			kwd.setStr("DO");
			kwd.setLex("do");
			doc.addKeyword(kwd);
		}

		AzureSearchDocumentImporter importer = new AzureSearchDocumentImporter();

		importer.setProperty("endpoint", ENDPOINT);
		importer.setProperty("admin_key", ADMIN_KEY);

		importer.importDocument(doc);

		importer.commit();

	}

	public void testCommit() {
		fail("Not yet implemented");
	}

	public void testClose() {
		fail("Not yet implemented");
	}

}

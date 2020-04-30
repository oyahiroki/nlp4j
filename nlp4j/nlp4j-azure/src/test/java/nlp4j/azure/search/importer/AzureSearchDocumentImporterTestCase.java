package nlp4j.azure.search.importer;

import java.util.Date;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.azure.search.TestEnv;
import nlp4j.impl.DefaultDocument;
import nlp4j.impl.DefaultKeyword;

/**
 * @author Hiroki Oya
 * @since 1.3
 */
public class AzureSearchDocumentImporterTestCase extends TestCase {

	/**
	 * ドキュメント追加正常時
	 * 
	 * @throws Exception 例外発生時
	 */
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
		importer.setProperty("service_name", TestEnv.SERVICE_NAME);
		importer.setProperty("index_name", TestEnv.INDEX_NAME);
		importer.setProperty("admin_key", TestEnv.ADMIN_KEY);

		importer.importDocument(doc);

		importer.commit();

	}

}

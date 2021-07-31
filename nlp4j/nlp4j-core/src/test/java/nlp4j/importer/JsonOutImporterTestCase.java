package nlp4j.importer;

import java.io.File;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.impl.DefaultDocument;

/**
 * @author Hiroki Oya
 * @created_at 2021-07-31
 */
public class JsonOutImporterTestCase extends TestCase {

	/**
	 * @throws Exception on Error
	 */
	public void testImportDocument001() throws Exception {
		File tempFile = File.createTempFile("nlp4j-test", ".xml");
		System.err.println(tempFile.getAbsolutePath());
//		tempFile.deleteOnExit();

		Document doc = new DefaultDocument();
		{
			doc.putAttribute("id", "001");
			doc.putAttribute("field1", 1);
			doc.putAttribute("field2", 1.1);
			doc.putAttribute("field3", "aaa");
			doc.putAttribute("text", "This is test.");
		}

		JsonOutImporter importer = new JsonOutImporter();
		importer.setProperty("file", tempFile.getAbsolutePath());

		importer.importDocument(doc);
	}

}

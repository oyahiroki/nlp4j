package nlp4j.importer;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.DocumentBuilder;
import nlp4j.DocumentImporter;

public class DocumentImporterBuilderTestCase extends TestCase {

	/**
	 * Created on 2024-09-01
	 */
	public void test001() throws Exception {

		DocumentImporter imt = (new DocumentImporterBuilder(DebugImporter.class)).p("test", "test").build();

		System.err.println(imt);

		Document doc = (new DocumentBuilder()).id("001").text("This is test.").build();

		imt.importDocument(doc);

		imt.commit();

		imt.close();

	}

}

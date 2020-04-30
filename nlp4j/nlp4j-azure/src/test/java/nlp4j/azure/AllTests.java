package nlp4j.azure;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import nlp4j.azure.search.admin.AzureSearchDocumentAdminTestCase;
import nlp4j.azure.search.importer.AzureSearchDocumentImporterTestCase;
import nlp4j.azure.search.searcher.AzureSearchTestCase;

public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		// $JUnit-BEGIN$
		suite.addTestSuite(AzureSearchDocumentImporterTestCase.class);
		suite.addTestSuite(AzureSearchTestCase.class);
		suite.addTestSuite(AzureSearchDocumentAdminTestCase.class);
		// $JUnit-END$
		return suite;
	}

}

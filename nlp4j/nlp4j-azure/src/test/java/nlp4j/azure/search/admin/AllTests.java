package nlp4j.azure.search.admin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import nlp4j.azure.search.admin.AzureSearchDocumentAdminTestCase;

public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(AzureSearchDocumentAdminTestCase.class);
		//$JUnit-END$
		return suite;
	}

}

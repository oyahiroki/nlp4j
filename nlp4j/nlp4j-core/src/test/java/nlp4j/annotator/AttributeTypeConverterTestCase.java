package nlp4j.annotator;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.util.DocumentUtil;

/**
 * Test for nlp4j.annotator.AttributeTypeConverter
 * 
 * @author Hiroki Oya
 *
 */
public class AttributeTypeConverterTestCase extends TestCase {

	@SuppressWarnings("rawtypes")
	Class target = AttributeTypeConverter.class;

	/**
	 * Test for<br>
	 * mapping=Date:yyyyMMdd,field_int1->Integer,field_int2->Integer
	 * 
	 * @throws Exception 例外発生時
	 */
	public void testAnnotateDocument001() throws Exception {
		AttributeTypeConverter ann = new AttributeTypeConverter();
		ann.setProperty("mapping", "date->Date:yyyyMMdd,field_int1->Integer,field_int2->Integer");
//		ann.setProperty("mapping", "field_int1->Integer,field_int2->Integer");

		Document doc = new DefaultDocument();
		doc.putAttribute("date", "20210401");
		doc.putAttribute("field_int1", "100");
		doc.putAttribute("field_int2", "200");

		System.err.println(DocumentUtil.toPrettyJsonString(doc));

		ann.annotate(doc);

		System.err.println(DocumentUtil.toPrettyJsonString(doc));

		assertTrue(doc.getAttributeAsString("date").startsWith("2021-04-01T00:00:00"));
		assertEquals(doc.getAttributeAsNumber("field_int1"), 100);
		assertEquals(doc.getAttributeAsNumber("field_int2"), 200);
	}

	/**
	 * Test for<br>
	 * mapping=Date:yyyyMMdd:UTC,field_int1->Integer,field_int2->Integer
	 * 
	 * @throws Exception 例外発生時
	 */
	public void testAnnotateDocument002() throws Exception {
		AttributeTypeConverter ann = new AttributeTypeConverter();
		ann.setProperty("mapping", "date->Date:yyyyMMdd:UTC,field_int1->Integer,field_int2->Integer");
//		ann.setProperty("mapping", "field_int1->Integer,field_int2->Integer");

		Document doc = new DefaultDocument();
		doc.putAttribute("date", "20210401");
		doc.putAttribute("field_int1", "100");
		doc.putAttribute("field_int2", "200");

		System.err.println(DocumentUtil.toPrettyJsonString(doc));

		ann.annotate(doc);

		System.err.println(DocumentUtil.toPrettyJsonString(doc));

		assertTrue(doc.getAttributeAsString("date").startsWith("2021-04-01"));
		assertEquals(doc.getAttributeAsNumber("field_int1"), 100);
		assertEquals(doc.getAttributeAsNumber("field_int2"), 200);
	}

	/**
	 * Test for<br>
	 * mapping=
	 * 
	 * @throws Exception 例外発生時
	 */
	public void testAnnotateDocument501() throws Exception {
		AttributeTypeConverter ann = new AttributeTypeConverter();
		ann.setProperty("mapping", "");
//		ann.setProperty("mapping", "date->Date:yyyyMMdd,field_int1->Integer,field_int2->Integer");
//		ann.setProperty("mapping", "field_int1->Integer,field_int2->Integer");

		Document doc = new DefaultDocument();
		doc.putAttribute("date", "20210401");
		doc.putAttribute("field_int1", "100");
		doc.putAttribute("field_int2", "200");

		System.err.println(DocumentUtil.toPrettyJsonString(doc));

		ann.annotate(doc);

		System.err.println(DocumentUtil.toPrettyJsonString(doc));

	}

}

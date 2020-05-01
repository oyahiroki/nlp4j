package nlp4j.annotator;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;
import nlp4j.Document;
import nlp4j.impl.DefaultDocument;
import nlp4j.util.DocumentUtil;
import nlp4j.util.JsonUtils;

/**
 * @author Hiroki Oya
 * @since 1.3
 */
public class DateDiffAnnotatorTestCase extends TestCase {

	/**
	 * @throws Exception 例外発生時
	 */
	public void testAnnotateDocument() throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date date1 = sdf.parse("2008-12-01");
		Date date2 = sdf.parse("2019-10-10");

		Document doc = new DefaultDocument();
		doc.putAttribute("date1", date1);
		doc.putAttribute("date2", date2);

		{
			DateDiffAnnotator ann = new DateDiffAnnotator();
			ann.setProperty("target1", "date1");
			ann.setProperty("target2", "date2");
			ann.setProperty("target", "date_diff_months");
			ann.setProperty("unit", "months");

			ann.annotate(doc);
		}
		{
			DateDiffAnnotator ann = new DateDiffAnnotator();
			ann.setProperty("target1", "date1");
			ann.setProperty("target2", "date2");
			ann.setProperty("target", "date_diff_years");
			ann.setProperty("unit", "years");

			ann.annotate(doc);
		}
		{
			DateDiffAnnotator ann = new DateDiffAnnotator();
			ann.setProperty("target1", "date1");
			ann.setProperty("target2", "date2");
			ann.setProperty("target", "date_diff_days");
			ann.setProperty("unit", "days");

			ann.annotate(doc);
		}

		System.err.println(JsonUtils.prettyPrint(DocumentUtil.toJsonObject(doc)));

	}

}

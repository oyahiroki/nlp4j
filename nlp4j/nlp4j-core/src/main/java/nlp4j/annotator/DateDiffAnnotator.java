/**
 * 
 */
package nlp4j.annotator;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.FieldAnnotator;

/**
 * 日付の差分を計算して追加する<br>
 * プロパティ<br>
 * target1 日付1（過去）のフィールド<br>
 * target2 日付2（未来）のフィールド<br>
 * target 日付2 - 日付1 をセットするフィールド<br>
 * 
 * @author Hiroki Oya
 * @since 1.3
 */
public class DateDiffAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, FieldAnnotator {

	String target = null;
	String target1 = null;
	String target2 = null;

	ChronoUnit unit = ChronoUnit.MONTHS;

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
		if (key != null) {
			if (key.equals("target")) {
				this.target = value;
			} //
			else if (key.equals("target1")) {
				this.target1 = value;
			} //
			else if (key.equals("target2")) {
				this.target2 = value;
			} else if (key.equals("unit")) {
				if (value == null) {

				} //
				else if (value.equals(ChronoUnit.HOURS.name().toLowerCase())) {
					this.unit = ChronoUnit.HOURS;
				} //
				else if (value.equals(ChronoUnit.DAYS.name().toLowerCase())) {
					this.unit = ChronoUnit.DAYS;
				} //
				else if (value.equals(ChronoUnit.MONTHS.name().toLowerCase())) {
					this.unit = ChronoUnit.MONTHS;
				} //
				else if (value.equals(ChronoUnit.YEARS.name().toLowerCase())) {
					this.unit = ChronoUnit.YEARS;
				} //
			}
		}

	}

	@Override
	public void annotate(Document doc) throws Exception {
		Date date1 = doc.getAttributeAsDate(this.target1);
		Date date2 = doc.getAttributeAsDate(this.target2);
		if (date1 != null && date2 != null) {
			LocalDateTime d1 = LocalDateTime.ofInstant(date1.toInstant(), ZoneId.systemDefault());
			LocalDateTime d2 = LocalDateTime.ofInstant(date2.toInstant(), ZoneId.systemDefault());
			long diff = this.unit.between(d1, d2);
			doc.putAttribute(this.target, diff);
		}

	}

}

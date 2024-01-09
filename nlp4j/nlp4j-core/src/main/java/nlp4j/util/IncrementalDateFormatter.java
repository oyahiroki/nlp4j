package nlp4j.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * created on: 2024-01-09
 * 
 * @author Hiroki Oya
 * @since 1.3.7.12
 */
public class IncrementalDateFormatter {

	DateTimeFormatter dtf;
	private int calendarUnit;
	private long count = 0;
	private long count_of_repeat;
	private Date date;

	private int amount = -1;

	SimpleDateFormat sdf;

	/**
	 * @param dateformat
	 * @param initialDate
	 * @param calendarUnit
	 * @param amount
	 * @param count_of_repeat
	 * @throws ParseException
	 */
	public IncrementalDateFormatter(String dateformat, String initialDate, int calendarUnit, int amount,
			long count_of_repeat) throws ParseException {
		this.amount = amount;
		this.calendarUnit = calendarUnit;
		this.dtf = DateTimeFormatter.ofPattern(dateformat);
		this.count_of_repeat = count_of_repeat;

		this.sdf = new SimpleDateFormat(dateformat);
		this.date = sdf.parse(initialDate);
	}

	public String next() {

		if (count > count_of_repeat) {
			return null;
		}

		String v = sdf.format(date);

		Calendar c = Calendar.getInstance();

		c.setTime(date);

		c.add(this.calendarUnit, this.amount);

		date = c.getTime();

		count++;

		return v;

	}

}

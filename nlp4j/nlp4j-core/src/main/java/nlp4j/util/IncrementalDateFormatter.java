package nlp4j.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class provides functionality to generate and manage a series of dates
 * incremented according to a specified unit and amount. It is useful for
 * creating sequences of dates for scheduling, simulations, or any application
 * that requires a series of future or past dates based on a given start date.
 * 
 * created on: 2024-01-09
 * 
 * @author Hiroki Oya
 * @since 1.3.7.12
 */
public class IncrementalDateFormatter {

	private int calendar_unit;
	private long count = 0;
	private long count_of_repeat;
	private Date date;

	private int calendar_add_amount = -1;

	private SimpleDateFormat sdf;
	private boolean reverse = false;

	/**
	 * Constructs an IncrementalDateFormatter with the specified date format,
	 * initial date, calendar unit for increment, amount to increment, and the
	 * number of repetitions.
	 * 
	 * @param dateformat      the format of the date as specified in
	 *                        {@link SimpleDateFormat}.
	 * @param initialDate     the initial date to start the sequence from, as a
	 *                        string in the specified format.
	 * @param calendarUnit    the calendar field to increment, as defined in
	 *                        {@link Calendar}.
	 * @param amount          the amount to add to the calendar field each step.
	 * @param count_of_repeat the number of times the date should be incremented.
	 * @throws ParseException if the initial date does not match the date format.
	 * 
	 */
	public IncrementalDateFormatter(String dateformat, String initialDate, int calendarUnit, int amount,
			long count_of_repeat) throws ParseException {
		this.calendar_add_amount = amount;
		this.calendar_unit = calendarUnit;
		this.count_of_repeat = count_of_repeat;

		this.sdf = new SimpleDateFormat(dateformat);
		this.date = sdf.parse(initialDate);
	}

	public IncrementalDateFormatter(String dateformat, String initialDate, int calendarUnit, int amount,
			long count_of_repeat, boolean reverse) throws ParseException {
		this.calendar_add_amount = amount;
		this.calendar_unit = calendarUnit;
		this.count_of_repeat = count_of_repeat;

		this.sdf = new SimpleDateFormat(dateformat);
		this.date = sdf.parse(initialDate);
		this.reverse = reverse;

		if (reverse == true) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			for (int n = 0; n < count_of_repeat; n++) {
				c.add(this.calendar_unit, this.calendar_add_amount);
			}
			date = c.getTime();
		}

	}

	/**
	 * Generates the next date in the sequence by incrementing the current date by
	 * the specified unit and amount. Returns null if the count of generated dates
	 * exceeds the specified repetition count.
	 * 
	 * @return the next date as a formatted string, or null if the sequence is
	 *         complete.
	 */
	public String next() {

		if (count > count_of_repeat) {
			return null;
		}

		String v = sdf.format(date);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (this.reverse == false) {
			c.add(this.calendar_unit, this.calendar_add_amount);
		} else {
			c.add(this.calendar_unit, this.calendar_add_amount * (-1));
		}
		date = c.getTime();
		count++;
		return v;
	}

}

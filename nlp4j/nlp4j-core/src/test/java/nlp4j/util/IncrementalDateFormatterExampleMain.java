package nlp4j.util;

import java.util.Calendar;

public class IncrementalDateFormatterExampleMain {

	public static void main(String[] args) {
		try {
			// Create an IncrementalDateFormatter instance
			IncrementalDateFormatter formatter //
					= new IncrementalDateFormatter("yyyy-MM-dd", "2024-01-01", Calendar.DAY_OF_MONTH, 1, 10);

			// Print dates incrementally
			String date;
			while ((date = formatter.next()) != null) {
				System.out.println(date);

				// Expected output
				// 2024-01-01
				// 2024-01-02
				// 2024-01-03
				// 2024-01-04
				// 2024-01-05
				// 2024-01-06
				// 2024-01-07
				// 2024-01-08
				// 2024-01-09
				// 2024-01-10
				// 2024-01-11
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

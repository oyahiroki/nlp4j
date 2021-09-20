package nlp4j.wiki;

import java.util.Locale;

@SuppressWarnings("javadoc")
public class HelloJavaLocaleMain {

	public static void main(String[] args) {
		Locale loc1 = new Locale("ja");
		{
			System.err.println(loc1);
			System.err.println(loc1.getLanguage());
			System.err.println(loc1.toLanguageTag());
			System.err.println(loc1.getISO3Language());
			System.err.println(loc1.getDisplayLanguage());
		}
		Locale loc2 = new Locale("jpn");
		{
			System.err.println(loc2.getLanguage());
			System.err.println(loc2.toLanguageTag());
			System.err.println(loc2.getISO3Language());
			System.err.println(loc2.getDisplayLanguage());
		}
		System.err.println(loc1.equals(loc2));

		System.err.println(Locale.JAPANESE.getLanguage());

	}

}

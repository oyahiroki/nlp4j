package nlp4j.wiki;

@SuppressWarnings("javadoc")
public class HelloJavaStringMain {

	static public void main(String[] args) {

		String line = "==={{noun}}=== <!--品詞 (名詞)-->";

		{
			int lastidx = line.lastIndexOf("==");
			if (lastidx != -1 && (line.length() > lastidx + 2)) {
				line = line.substring(0, lastidx + 2);
			}
		}

		System.err.println(line);

	}

}

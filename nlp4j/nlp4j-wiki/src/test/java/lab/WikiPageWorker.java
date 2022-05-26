package lab;

import nlp4j.wiki.WikiPage;

public class WikiPageWorker {

	String longestTitle = null;
	int longestLength = -1;

	public void put(WikiPage page) {
		String t = page.getTitle();

		String text = page.getText();

		if (text != null) {

			if (longestLength < text.length()) {
				longestLength = text.length();
				longestTitle = t;
			}

		} else {
			return;
		}

	}

	public void print() {
		System.err.println(longestTitle);
		System.err.println(longestLength);
	}

}

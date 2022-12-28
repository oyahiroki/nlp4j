package nlp4j.dictionary;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;

/**
 * @author Hiroki Oya
 * @since 1.3.7.5
 */
public class UserDictionaryStringMatchAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	UserDictionaryStringMatcher matcher;

	public UserDictionaryStringMatchAnnotator() {
		this.matcher = new UserDictionaryStringMatcher();
	}

	@Override
	public void annotate(Document doc) throws Exception {
		for (String target : super.targets) {
			String text = doc.getAttributeAsString(target);
			if (text == null || text.isEmpty()) {
				continue;
			}
			List<Keyword> kwds = this.matcher.parse(text);
			doc.addKeywords(kwds);
		}
	}

	@Override
	public void setProperty(String key, String value) {

		super.setProperty(key, value);

		// IF KEY = files
		if ("files".equals(key)) {

			logger.info("key:" + key + ",value:" + value);

			File file = new File(value);

			if (file.exists() == false) {
				return;
			}

			// FILE EXISTS
			try {
				List<String> lines = FileUtils.readLines(file, "UTF-8");
				// FOR EACH (LINE)
				for (String line : lines) {
					if (line.trim().isEmpty() == true) {
						continue;
					}
					String[] ss = line.split("\t");
					if (ss.length < 2) {
						continue;
					}
					String facet = ss[0];
					String lex = ss[1];
					String[] synonyms = null;
					if (ss.length > 2) {
						synonyms = Arrays.copyOfRange(ss, 2, ss.length);
					}
					if (synonyms == null) {
						this.matcher.put(facet, lex);
					} else {
						this.matcher.put(facet, lex, synonyms);
					}
				} // END OF FOR EACH (LINE)
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			} // END OF TRY

		} // END OF (IF KEY = files)

		else if ("target".equals(key)) {
			if (super.targets.contains(value) == false) {
				super.targets.add(value);
			}
		}

	}

	@Override
	public String toString() {
		return "UserDictionaryStringMatchAnnotator [matcher=" + matcher + "]";
	}

}

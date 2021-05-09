/**
 * 
 */
package nlp4j.pattern;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;

/**
 * @author Hiroki Oya
 * @created_at 2021-05-05
 */
public class UserPatternAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	File dir;
	List<File> files = new ArrayList<File>();

	PatternMatcher patternMatcher;

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
		if ("dir".equals(key)) {
			File dir = new File(value);
			if (dir.isDirectory()) {
				this.dir = dir;
			} else {
				logger.warn("Not dir: " + dir.getAbsolutePath());
			}
		} //
		else if ("file".equals(key)) {
			File file = new File(value);
			if (file.exists() == true) {
				this.files.add(file);
			} else {
				logger.warn("Not exists: " + file.getAbsolutePath());
			}
		}
	}

	private List<Pattern> patterns;

	@Override
	public void annotate(Document doc) throws Exception {

		if (patterns == null) {
			initialize();
		}

		ArrayList<Keyword> newKwds = new ArrayList<>();

		for (Keyword kwd : doc.getKeywords()) {
			if (kwd instanceof KeywordWithDependency) {
				KeywordWithDependency kw = (KeywordWithDependency) kwd;

				for (Pattern pattern : this.patterns) {

					List<Keyword> kwds0 = PatternMatcher.match(kw, pattern);

					if (kwds0 != null) {
						newKwds.addAll(kwds0);
					}
				}
			}
		}

		doc.addKeywords(newKwds);

	}

	private void initialize() {
		this.patterns = new ArrayList<>();

		if (this.dir != null) {
			File[] xmlFiles = dir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if (name.endsWith(".xml")) {
						return true;
					} else {
						return false;
					}
				}
			});
			for (File xmlFile : xmlFiles) {
				List<Pattern> patterns = PatternReader.readFromXml(xmlFile);
				this.patterns.addAll(patterns);
			}
		}
		if (this.files != null) {
			for (File file : files) {
				List<Pattern> patterns = PatternReader.readFromXml(file);
				this.patterns.addAll(patterns);
			}
		}
	}

}

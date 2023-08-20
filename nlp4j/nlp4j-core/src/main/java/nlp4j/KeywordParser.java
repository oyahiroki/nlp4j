package nlp4j;

import java.io.Closeable;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * created on: 2023-05-30
 * 
 * @author Hiroki Oya
 * @since 1.3.7.9
 */
public class KeywordParser implements Closeable {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

//	private DocumentAnnotator annotator;
	private final List<DocumentAnnotator> annotators = new ArrayList<>();

	public KeywordParser(DocumentAnnotator annotator) {
		super();
//		this.annotator = annotator;
		annotators.add(annotator);
	}

	/**
	 * @param annotators
	 * @since 1.3.7.12
	 */
	public KeywordParser(DocumentAnnotator... annotators) {
		for (DocumentAnnotator ann : annotators) {
			this.annotators.add(ann);
		}
	}

	/**
	 * @param text
	 * @return
	 * @throws Exception
	 * @since 1.3.7.12
	 */
	public List<Keyword> parse(String text) throws Exception {
		return parse(text, false);
	}

	/**
	 * @param text
	 * @param allowduplicate
	 * @return
	 * @since 1.3.7.12
	 */
	public List<Keyword> parseIgnoreException(String text, boolean allowduplicate) {
		try {
			return parse(text, allowduplicate);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return new ArrayList<>();
		}

	}

	/**
	 * @param text
	 * @param allowduplicate
	 * @return
	 * @throws Exception
	 * @since 1.3.7.12
	 */
	public List<Keyword> parse(String text, boolean allowduplicate) throws Exception {
		List<Keyword> kwds = new ArrayList<>();
		Set<String> lex_ss = new HashSet<>();
		for (DocumentAnnotator ann : annotators) {
			Document doc = (new DocumentBuilder()).text(text).build();
			ann.annotate(doc);

			if (allowduplicate == true) {
				kwds.addAll(doc.getKeywords());
			} else {
				for (Keyword kwd : doc.getKeywords()) {
					String lex = kwd.getLex();
					if (lex_ss.contains(lex) == false) {
						kwds.add(kwd);
						lex_ss.add(lex);
					}
				}
			}
		}
		return kwds;
	}

	@Override
	public void close() throws IOException {
		for (DocumentAnnotator ann : annotators) {
			if (ann instanceof Closeable) {
				try {
					((Closeable) ann).close();
				} catch (Exception e) {
					// do nothing
				}
			}
		}
	}

}

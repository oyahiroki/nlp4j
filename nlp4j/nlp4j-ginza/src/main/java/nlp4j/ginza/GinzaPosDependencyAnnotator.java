package nlp4j.ginza;

import java.util.List;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.KeywordWithDependency;
import nlp4j.KeywordWithDependencyParser;
import nlp4j.NlpServiceResponse;
import nlp4j.annotator.DependencyAnnotator;

/**
 * Defalt endPoint: http://localhost:8888/
 * 
 * @author Hiroki Oya
 *
 */
public class GinzaPosDependencyAnnotator extends AbstractGinzaAnnotator
		implements DocumentAnnotator, DependencyAnnotator {

	private String endPoint = "http://localhost:8888/";

	private boolean extract_keyword = false;

	GinzaNlpServiceViaHttp ginza = new GinzaNlpServiceViaHttp(endPoint);

	@Override
	public void annotate(Document doc) throws Exception {
		for (String target : super.targets) {
			String text = doc.getAttributeAsString(target);
			NlpServiceResponse res = ginza.process(text); // throws IOException
			if (res != null) {
				List<Keyword> kwds = res.getKeywords();
				doc.addKeywords(kwds);

				if (this.extract_keyword == true) {

					for (Keyword kwd : kwds) {
						if (kwd instanceof KeywordWithDependency) {
							KeywordWithDependency kwdd = (KeywordWithDependency) kwd;
							List<Keyword> kww = KeywordWithDependencyParser.parse(kwdd);
//							List<Keyword> kww = KeywordWithDependencyParser.flatten(kwdd);
							doc.addKeywords(kww);
						}
					}

				}

			}
		}
	}

	/**
	 * <pre>
	 * Example: endPoint = http://localhost:8888/
	 * Example: extact_keyword = true
	 * </pre>
	 * 
	 * @param key   : "endPoint"
	 * @param value : like "http://localhost:8888/"
	 */
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
		if ("endpoint".equals(key.toLowerCase())) {
			this.endPoint = value;
			this.ginza = new GinzaNlpServiceViaHttp(this.endPoint);
		} //

		else if ("extract_keyword".equals(key.toLowerCase())) {
			this.extract_keyword = Boolean.parseBoolean(value);
		}

	}

}

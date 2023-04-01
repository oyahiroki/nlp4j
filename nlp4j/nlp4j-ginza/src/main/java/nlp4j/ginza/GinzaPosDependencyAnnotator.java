package nlp4j.ginza;

import java.util.List;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.Keyword;
import nlp4j.NlpServiceResponse;
import nlp4j.annotator.DependencyAnnotator;

public class GinzaPosDependencyAnnotator extends AbstractGinzaAnnotator
		implements DocumentAnnotator, DependencyAnnotator {

	private static final String endPoint = "http://localhost:8888/";
	GinzaNlpServiceViaHttp ginza = new GinzaNlpServiceViaHttp(endPoint);

	@Override
	public void annotate(Document doc) throws Exception {

//		super.annotate(doc);

		for (String target : super.targets) {
//			System.err.println("target: " + target);

			String text = doc.getAttributeAsString(target);

			NlpServiceResponse res = ginza.process(text);

			List<Keyword> kwds = res.getKeywords();
			doc.addKeywords(kwds);

		}

	}

}

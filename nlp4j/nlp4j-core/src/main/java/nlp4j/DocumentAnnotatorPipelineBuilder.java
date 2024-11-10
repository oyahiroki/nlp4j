package nlp4j;

import nlp4j.impl.DefaultDocumentAnnotatorPipeline;

public class DocumentAnnotatorPipelineBuilder {

	private final DefaultDocumentAnnotatorPipeline pp = new DefaultDocumentAnnotatorPipeline();

	public DocumentAnnotatorPipelineBuilder add(DocumentAnnotator ann) {
		this.pp.add(ann);
		return this;
	}

	public DocumentAnnotator build() {
		return this.pp;
	}

}

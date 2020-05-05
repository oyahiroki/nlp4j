package nlp4j.impl;

import java.util.List;

import nlp4j.Document;

/**
 * Runnable 対応版
 * 
 * @author Hiroki Oya
 * @since 1.3
 */
public class DefaultDocumentAnnotatorPipelineRunnable extends DefaultDocumentAnnotatorPipeline implements Runnable {

	/**
	 * 処理対象のドキュメント
	 */
	private List<Document> docs;

	/**
	 * @return 処理対象のドキュメント
	 */
	public List<Document> getDocs() {
		return docs;
	}

	/**
	 * @param docs 処理対象のドキュメント
	 */
	public void setDocs(List<Document> docs) {
		this.docs = docs;
	}

	@Override
	public void run() {

		if (docs != null) {
			try {
				annotate(docs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}

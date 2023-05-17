package nlp4j.annotator.concurrent;

import java.io.Closeable;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.util.CollectionUtils;

/**
 * 並列処理を行う <br>
 * 全件の処理を終えるには #close を呼ぶ
 * 
 * 
 * @author Hiroki Oya
 *
 */
public class ConcurrentDocumentAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, Closeable {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * 並列処理に利用するAnnotator
	 */
	private List<DocumentAnnotator> anns = null;

	List<Document> docBuffer = new ArrayList<>();
	boolean[] isActiveAnnotator = null;

	/**
	 * @param anns 並列処理に利用するAnnotator
	 */
	public ConcurrentDocumentAnnotator(List<DocumentAnnotator> anns) {
		Objects.nonNull(anns);

		this.anns = anns;

		{
			isActiveAnnotator = new boolean[anns.size()];
			Arrays.fill(isActiveAnnotator, true);
		}
	}

	@Override
	public void annotate(Document doc) throws Exception {
		docBuffer.add(doc);
		if (docBuffer.size() >= getNumberOfThreads()) {
			annotate(docBuffer); // throws Exception
			docBuffer = new ArrayList<>();
		}
	}

	@Override
	public void annotate(List<Document> docs) throws Exception {

		int nThreads = getNumberOfThreads();
		List<List<Document>> ddd = CollectionUtils.partition(docs, nThreads);

		for (List<Document> dd : ddd) {

			ExecutorService executor = Executors.newFixedThreadPool(nThreads);
			try {

				List<Future<AnnotationResult>> taskList = new ArrayList<Future<AnnotationResult>>();

				for (int n = 0; n < dd.size(); n++) {
					DocumentAnnotator ann = null;
					if (n < this.anns.size()) {
						ann = this.anns.get(n);
					} else {
						ann = this.anns.get(0);
					}

					Document doc = docs.get(n);
					{
						AnnotateCall p1 = new AnnotateCall(ann, doc, n);
						taskList.add(executor.submit(p1));
					}
				}

				// タスク処理中にExceptionが発生したかどうかを確認する
				for (int idxTask = 0; idxTask < taskList.size(); idxTask++) {
					Future<AnnotationResult> f = taskList.get(idxTask);
					try {
						f.get(); // throws ExecutionException
					} catch (ExecutionException exE) {

//					exE.printStackTrace();

						Throwable cause = exE.getCause();
						if (cause instanceof AnnotationException) {
							AnnotationException ae = (AnnotationException) cause;
							int annotatorIndex = ae.getAnnotatorIndex();

							System.err.println(cause.getCause());

							// TODO 並列処理で例外が発生したときにリトライする
							logger.error("Exception on Annotator: " + annotatorIndex);
							logger.error(exE);
//						exE.printStackTrace();

							synchronized (this) {
								this.anns.remove(annotatorIndex);
							}

							if (this.anns == null || this.anns.size() == 0) {
								throw exE;
							} //
							else {
								logger.info("Continue process..");
							}
						}

					}
				}

			} finally {
				executor.shutdown();
				executor.awaitTermination(1, TimeUnit.MINUTES);
			}

		}

		if (anns != null) {

		}
	}

	@Override
	public void close() throws IOException {

		if (docBuffer.size() > 0) {
			try {
				annotate(docBuffer);
			} catch (Exception e) {
				throw new IOException(e);
			}
			docBuffer = new ArrayList<>();
		}

		if (anns != null) {
			for (DocumentAnnotator ann : anns) {
				if (ann instanceof Closeable) {
					((Closeable) ann).close();
				}
			}
		}
	}

	public int getNumberOfThreads() {
		return (this.anns == null) ? 0 : this.anns.size();
	}

}

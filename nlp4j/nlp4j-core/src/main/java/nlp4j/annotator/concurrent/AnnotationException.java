package nlp4j.annotator.concurrent;

public class AnnotationException extends Exception {

	private int annotatorIndex = -1;

	private static final long serialVersionUID = 1L;

	public int getAnnotatorIndex() {
		return annotatorIndex;
	}

	public void setAnnotatorIndex(int annotatorIndex) {
		this.annotatorIndex = annotatorIndex;
	}

	@Override
	public synchronized Throwable initCause(Throwable cause) {
		// TODO Auto-generated method stub
		return super.initCause(cause);
	}

}

package nlp4j.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * created on: 2023-10-22
 * 
 * @author Hiroki Oya
 *
 */
public class MultiIOException extends IOException {

	private static final long serialVersionUID = -6357390279156212009L;
	private final List<Throwable> exceptions = new ArrayList<>();

	public MultiIOException() {
		super();
	}

	public MultiIOException(String message) {
		super(message);
	}

	public void addException(Throwable exception) {
		exceptions.add(exception);
	}

	public List<Throwable> getExceptions() {
		return Collections.unmodifiableList(exceptions);
	}

	public int size() {
		return exceptions.size();
	}
}

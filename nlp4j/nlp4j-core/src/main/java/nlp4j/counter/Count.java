package nlp4j.counter;

/**
 * @author Hiroki Oya
 * @created_at 2021-07-13
 * @param <T> class for count
 */
public class Count<T> {

	T obj;
	int count = -1;

	/**
	 * @param obj   target of count
	 * @param count of object
	 */
	public Count(T obj, int count) {
		super();
		this.obj = obj;
		this.count = count;
	}

	public T getValue() {
		return this.obj;
	}

	public int getCount() {
		return count;
	}

	@Override
	public String toString() {
		return "Count [obj=" + obj + ", count=" + count + "]";
	}

}

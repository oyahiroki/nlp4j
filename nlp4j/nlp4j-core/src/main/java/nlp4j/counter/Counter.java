package nlp4j.counter;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * created on : 2021-07-13
 * 
 * @author Hiroki Oya
 * @param <T> Class for counting
 * @since 1.3.2
 */
public class Counter<T> {
	int countAll = 0;
	HashMap<T, Integer> objCounter;
	private String description;

	/**
	 * Constructor
	 */
	public Counter() {
		this.objCounter = new HashMap<T, Integer>();
	}

	public Counter(String description) {
		this.objCounter = new HashMap<T, Integer>();
		this.description = description;
	}

	/**
	 * @param obj to be counted
	 */
	public void add(T obj) {
		countAll++;
		Integer count = objCounter.get(obj);
		// IF(COUNT != NULL) THEN
		if (count != null) {
			objCounter.put(obj, count + 1);
		}
		// ELSE
		else {
			objCounter.put(obj, 1);
		}
	}

	public void decrement(T obj) {

		Integer count = objCounter.get(obj);
		// IF(COUNT != NULL) THEN
		if (count != null && count > 1) {
			objCounter.put(obj, count - 1);
			countAll--;
		}
		// ELSE
		else {
			objCounter.put(obj, 0);
		}
	}

	/**
	 * @param obj for counting
	 * @return Count of object
	 */
	public int getCount(T obj) {
		Integer count = objCounter.get(obj);
		// IF(COUNT != NULL) THEN
		if (count != null) {
			return count;
		}
		// ELSE
		else {
			return 0;
		}
	}

	/**
	 * Sort by value
	 * 
	 * @return
	 */
	public List<Count<T>> getCountList() {
		List<T> objList = getObjectList();
		ArrayList<Count<T>> counts = new ArrayList<Count<T>>();
		for (T o : objList) {
			counts.add(new Count<T>(o, getCount(o)));
		}
		return counts;
	}

	/**
	 * Sort by count 降順(DESC)
	 * 
	 * @return
	 */
	public List<Count<T>> getCountListSorted() {
		List<T> objList = getObjectList();
		Collections.sort(objList, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return getCount(o2) - getCount(o1);
			}
		});
		ArrayList<Count<T>> counts = new ArrayList<Count<T>>();
		for (T o : objList) {
			counts.add(new Count<T>(o, getCount(o)));
		}
		return counts;
	}

	/**
	 * Sort by count 昇順(ASC)
	 * 
	 * @return
	 */
	public List<Count<T>> getCountListSortedAsc() {
		List<T> objList = getObjectList();
		Collections.sort(objList, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return (getCount(o2) - getCount(o1)) * (-1);
			}
		});
		ArrayList<Count<T>> counts = new ArrayList<Count<T>>();
		for (T o : objList) {
			counts.add(new Count<T>(o, getCount(o)));
		}
		return counts;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * @return Object as List
	 */
	public List<T> getObjectList() {
		return new ArrayList<T>(objCounter.keySet());
	}

	/**
	 * @return
	 */
	public List<T> getObjectListSorted() {
		List<T> objList = getObjectList();
		Collections.sort(objList, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return getCount(o2) - getCount(o1);
			}
		});
		return objList;
	}

	public void increment(T obj) {
		add(obj);
	}

	/**
	 * created on : 2023-06-25
	 * 
	 * @since 1.3.7.9
	 */
	public void print() {

		print(new PrintWriter(System.out, true));

	}

	/**
	 * created on : 2023-09-30
	 * 
	 * @since 1.3.7.12
	 */
	public void print(PrintWriter pw) {
		if (this.description != null) {
			pw.println(this.description);
		}
		List<Count<T>> list = getCountListSorted();
		for (Count<T> c : list) {
			pw.println("value=" + c.getValue() + ",count=" + c.getCount() + ",ratio="
					+ String.format("%.2f", ((double) c.getCount() / countAll * 100)));
		}

	}

	/**
	 * created on : 2023-07-04
	 * 
	 * @since 1.3.7.9
	 */
	public void printValues(String delimiter) {
//		if (this.description != null) {
//			System.out.println(this.description);
//		}
//		List<T> values = getObjectListSorted();
//		List<String> valuesString = new ArrayList<>(values.size());
//		values.forEach(v -> {
//			valuesString.add(v.toString());
//		});
//		System.out.println(String.join(delimiter, valuesString));
		System.out.println(toValues(delimiter));
	}

	public String toValues(String delimiter) {
		StringBuilder sb = new StringBuilder();

		if (this.description != null) {
			sb.append(this.description + "\n");
		}
		List<T> values = getObjectListSorted();
		List<String> valuesString = new ArrayList<>(values.size());
		values.forEach(v -> {
			valuesString.add(v.toString());
		});
		sb.append(String.join(delimiter, valuesString) + "\n");
		return sb.toString();
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

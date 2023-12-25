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

	private int countAll = 0;
	private HashMap<T, Integer> objCounter;
	private String description;

	/**
	 * Constructor
	 */
	public Counter() {
		this.objCounter = new HashMap<T, Integer>();
	}

	/**
	 * @param description
	 */
	public Counter(String description) {
		this.objCounter = new HashMap<T, Integer>();
		this.description = description;
	}

	/**
	 * @param obj to be counted
	 */
	public void add(T obj) {
		add(obj, 1);
	}

	/**
	 * @param obj to be counted
	 * @param cnt count of object
	 */
	public void add(T obj, int cnt) {
		countAll += cnt;
		Integer count = objCounter.get(obj);
		// IF(COUNT != NULL) THEN
		if (count != null) {
			objCounter.put(obj, count + cnt);
		}
		// ELSE
		else {
			objCounter.put(obj, cnt);
		}
	}

	/**
	 * created on : 2023-12-24
	 * 
	 * @param counter2
	 * @since 1.3.7.12
	 */
	public void addAll(Counter<T> counter2) {
		if (counter2.getCountList() != null) {
			for (Count<T> count : counter2.getCountList()) {
				this.add(count.getValue(), count.getCount());
			}
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

	/**
	 * @param obj
	 * @return ratio of count
	 * @since 1.3.7.12
	 */
	public double getRatio(T obj) {
		int n = getCount(obj);
		double d = (double) n / this.countAll;
		return d;
	}

	public void increment(T obj) {
		add(obj);
	}

	/**
	 * @param cc
	 * @return One of max count object
	 * @since 1.3.7.12
	 */
	public Count<T> ofMax(List<T> cc) {
		int maxCount = -1;
		T maxObj = null;
		if (cc == null) {
			return null;
		} else {
			for (T t : cc) {
				int c = getCount(t);
				if (c > maxCount) {
					maxCount = c;
					maxObj = t;
				}
			}
			return new Count<>(maxObj, maxCount);
		}
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
//		if (this.description != null) {
//			pw.println(this.description);
//		}
//		List<Count<T>> list = getCountListSorted();
//		for (Count<T> c : list) {
//			pw.println("value=" + c.getValue() + ",count=" + c.getCount() + ",ratio="
//					+ String.format("%.2f", ((double) c.getCount() / countAll * 100)));
//		}
		pw.println(this.toString());

	}

	/**
	 * Print values only, count not printed <br/>
	 * <br/>
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

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @since 1.3.7.12
	 * @return size of counted object
	 */
	public int size() {
		return (this.objCounter != null) ? this.objCounter.size() : 0;
	}

	/**
	 * created on : 2023-10-09
	 * 
	 * @since 1.3.7.12
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.description != null) {
			sb.append(this.description + "\n");
		}
		List<Count<T>> list = getCountListSorted();
		if (list != null) {
			for (Count<T> c : list) {
				sb.append("value=" + c.getValue() + ",count=" + c.getCount() + ",ratio="
						+ String.format("%.2f", ((double) c.getCount() / countAll * 100)) + "\n");
			}
		}
		return sb.toString();
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
}

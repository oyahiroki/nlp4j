package nlp4j.counter;

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
	HashMap<T, Integer> objCounter;

	/**
	 * Constructor
	 */
	public Counter() {
		this.objCounter = new HashMap<T, Integer>();
	}

	/**
	 * @param obj to be counted
	 */
	public void add(T obj) {
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

	public void increment(T obj) {
		add(obj);
	}

	public void decrement(T obj) {
		Integer count = objCounter.get(obj);
		// IF(COUNT != NULL) THEN
		if (count != null && count > 1) {
			objCounter.put(obj, count - 1);
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
	 * Sort by count
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
	 * Sort by count
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
}

package nlp4j.wiki;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <pre>
 * Wiki Index
 * Wiki のインデックス情報
 * created_at 2021-06-25
 * </pre>
 * 
 * @author Hiroki Oya
 */
public class WikiIndex {

	ArrayList<Long> blockIndex = new ArrayList<>();

	long dataLength = -1;;
	long lastBlockNum = -1;

	// Item -> WikiIndexItem, 見出し -> WikiIndexItem
	HashMap<String, WikiIndexItem> mapWikiIndexItem;

	// List of Wiki Item Titles 見出し
	ArrayList<String> wikiItemTitles;

	// List of WikiIndexItem
	ArrayList<WikiIndexItem> wikiIndexItems;

	/**
	 * Initialize : capacity is 1000 * 1000
	 */
	public WikiIndex() {
		this.mapWikiIndexItem = new HashMap<>(1000 * 1000);
		wikiItemTitles = new ArrayList<>(1000 * 1000);
		wikiIndexItems = new ArrayList<>(1000 * 1000);
	}

	/**
	 * @param capacity : initial capacity of index
	 */
	public WikiIndex(int capacity) {
		this.mapWikiIndexItem = new HashMap<>();
		wikiItemTitles = new ArrayList<>(capacity);
		wikiIndexItems = new ArrayList<>(capacity);
	}

	/**
	 * @param wikiIndexItem to be added
	 */
	public void add(WikiIndexItem wikiIndexItem) {

//		System.err.println("item: " + wikiIndexItem.getItem());

		this.mapWikiIndexItem.put(wikiIndexItem.getTitle(), wikiIndexItem);
		this.wikiItemTitles.add(wikiIndexItem.getTitle());
		this.wikiIndexItems.add(wikiIndexItem);

		if (this.lastBlockNum != wikiIndexItem.getBlockNum()) {
			this.lastBlockNum = wikiIndexItem.getBlockNum();
			blockIndex.add(wikiIndexItem.getBlockNum());
		}
	}

	public ArrayList<WikiIndexItem> getWikiIndexItems() {
		return wikiIndexItems;
	}

	/**
	 * @param item label of wiki index
	 * @return item
	 */
	public WikiIndexItem getItem(String item) {

		WikiIndexItem foundItem = this.mapWikiIndexItem.get(item);

		if (foundItem == null) {
			return null;
		} //
		else {
			long blockNum = foundItem.getBlockNum();
			int idx = this.blockIndex.indexOf(blockNum);
			if (idx < 0) {
				// no case;
				return foundItem;
			} //
			else if (idx >= 0 && idx != (this.blockIndex.size() - 1)) {
				long nextBlock = this.blockIndex.get(idx + 1);
				foundItem.setNextBlock(nextBlock);
				return foundItem;
			} //
			else {
				if (this.dataLength != -1) {
					foundItem.setNextBlock(this.dataLength);
				}
				return foundItem;
			}
		}

	}

	public ArrayList<String> getWikiItemTitles() {
		return wikiItemTitles;
	}

	/**
	 * @param item of wiki index
	 * @return has item (true) or not (false)
	 */
	public boolean hasItem(String item) {
		return (this.mapWikiIndexItem.get(item) != null);
	}

	/**
	 * @param dataLength
	 */
	public void setDataLength(long dataLength) {
		this.dataLength = dataLength;
	}

	/**
	 * @return item size of wiki index
	 */
	public int size() {
		return this.mapWikiIndexItem.size();
	}

	@Override
	public String toString() {
		return "WikiIndex [wikiItemTitles=" + ((wikiItemTitles == null) ? 0 : wikiItemTitles.size()) + "]";
	}

}

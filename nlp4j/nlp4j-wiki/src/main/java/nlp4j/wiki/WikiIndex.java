package nlp4j.wiki;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Hiroki Oya
 * @created_at 2021-06-25
 */
public class WikiIndex {

	ArrayList<Long> blockIndex = new ArrayList<>();

	long dataLength = -1;;
	long lastBlockNum = -1;
	HashMap<String, WikiIndexItem> map;

	/**
	 * 
	 */
	public WikiIndex() {
		this.map = new HashMap<>(1000 * 1000);
	}

	/**
	 * @param initial capasity of index
	 */
	public WikiIndex(int capasity) {
		this.map = new HashMap<>();
	}

	/**
	 * @param wikiIndexItem
	 */
	public void add(WikiIndexItem wikiIndexItem) {
		this.map.put(wikiIndexItem.getItem(), wikiIndexItem);
		if (this.lastBlockNum != wikiIndexItem.getBlockNum()) {
			this.lastBlockNum = wikiIndexItem.getBlockNum();
			blockIndex.add(wikiIndexItem.getBlockNum());
		}
	}

	/**
	 * @param item label of wiki index
	 * @return item
	 */
	public WikiIndexItem getItem(String item) {

		WikiIndexItem foundItem = this.map.get(item);

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

	/**
	 * @param item of wiki index
	 * @return has item (true) or not (false)
	 */
	public boolean hasItem(String item) {
		return (this.map.get(item) != null);
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
		return this.map.size();
	}

}

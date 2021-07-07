package nlp4j.wiki;

public class WikiIndexItem {

	private long blockNum;
	private long itemID;
	private String item;

	private long nextBlock = -1;

	/**
	 * @param blockNum
	 * @param itemID
	 * @param item
	 */
	public WikiIndexItem(long blockNum, long itemID, String item) {
		super();
		this.blockNum = blockNum;
		this.itemID = itemID;
		this.item = item;
	}

	public long getBlockNum() {
		return blockNum;
	}

	public long getItemID() {
		return itemID;
	}

	public String getItem() {
		return item;
	}

	public void setNextBlock(long nextBlock) {
		this.nextBlock = nextBlock;

	}

	public long getNextBlock() {
		return nextBlock;
	}

	public long getSize() {
		if (this.blockNum != -1 && this.nextBlock != -1) {
			return (this.nextBlock - this.blockNum);
		} else {
			return -1;
		}
	}

	@Override
	public String toString() {
		return "WikiIndexItem [blockNum=" + blockNum + ", itemID=" + itemID + ", item=" + item + ", nextBlock="
				+ nextBlock + "]";
	}

}

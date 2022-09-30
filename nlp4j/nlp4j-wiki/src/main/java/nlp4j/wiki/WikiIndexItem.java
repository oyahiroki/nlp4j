package nlp4j.wiki;

public class WikiIndexItem {

	private long blockNum;
	private long itemID;
	private String title;

	private String namespace;

	private long nextBlock = -1;

	/**
	 * @param blockNum
	 * @param itemID
	 * @param title
	 */
	public WikiIndexItem(long blockNum, long itemID, String title) {
		super();
		this.blockNum = blockNum;
		this.itemID = itemID;
		this.title = title;

		int idxOfNamespace = title.indexOf(":");

		if (idxOfNamespace != -1) {
			this.namespace = title.substring(0, idxOfNamespace);
		}
	}

	public long getBlockNum() {
		return blockNum;
	}

	public long getItemID() {
		return itemID;
	}

	public String getNamespace() {
		return namespace;
	}

	public long getNextBlock() {
		return nextBlock;
	}

	/**
	 * @return Size: difference of next block number and this block number
	 */
	public long getSize() {
		if (this.blockNum != -1 && this.nextBlock != -1) {
			return (this.nextBlock - this.blockNum);
		} else {
			return -1;
		}
	}

	public String getTitle() {
		return title;
	}

	public void setNextBlock(long nextBlock) {
		this.nextBlock = nextBlock;

	}

	@Override
	public String toString() {
		return "WikiIndexItem [" //
				+ "blockNum=" + blockNum //
				+ ", itemID=" + itemID //
				+ ", title=" + title //
				+ ", namespace=" + namespace //
				+ ", nextBlock=" + nextBlock //
				+ "]";
	}

}

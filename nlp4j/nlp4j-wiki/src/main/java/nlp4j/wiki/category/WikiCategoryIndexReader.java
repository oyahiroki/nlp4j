package nlp4j.wiki.category;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.util.FileUtils;

/**
 * <pre>
 * カテゴリ情報を読み込む
 * Read wiki categories index file
 * </pre>
 * 
 * created on 2023-02-01
 * 
 * @author Hiroki Oya
 */
public class WikiCategoryIndexReader {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	Map<String, String> mapIdTitle = new HashMap<String, String>();
	Map<String, WikiCategory> mapTitleObj = new HashMap<>();

	public WikiCategory getCategory(String categoryTitle) {
		return mapTitleObj.get(categoryTitle);
	}

	/**
	 * カテゴリ構造を読み込む
	 * 
	 * @param indexFile
	 * @param categoryFile
	 * @throws IOException
	 */
	public WikiCategoryIndexReader(File indexFile, File categoryFile) throws IOException {
		logger.info("Reading Files ...");
		long time1 = System.currentTimeMillis();

		{
			BufferedReader br = FileUtils.openTextFileAsBufferedReader(indexFile);
			BufferedReader br2 = FileUtils.openTextFileAsBufferedReader(categoryFile);

			String s;

			Set<String> used = new HashSet<String>();

			// 同名のカテゴリは存在しないことを確認済

			// カテゴリ一覧を読み込み
			while ((s = br.readLine()) != null) {
//				System.err.println(s);
				if (s.isEmpty()) {
					continue;
				}
				int idx = s.indexOf(':');
				if (idx == -1) {
					throw new RuntimeException("" + s);
				}
				String categoryId = s.substring(0, idx).trim();
				String categoryTitle = s.substring(idx + 1).trim();

				mapIdTitle.put(categoryId, categoryTitle);

				WikiCategory catObj = new WikiCategory(categoryId, categoryTitle);
				mapTitleObj.put(categoryTitle, catObj);

				if (used.contains(categoryTitle)) {
					throw new RuntimeException("" + categoryTitle);
				} else {
					used.add(categoryTitle);
				}
			}

			// カテゴリの親情報を読み込み（親が無いカテゴリも存在する）
			while ((s = br2.readLine()) != null) {
				int idx = s.indexOf(':');
				if (idx == -1) {
					throw new RuntimeException("" + s);
				}
//				String categoryId = s.substring(0, idx);
				String s2 = s.substring(idx + 1);
				int idx2 = s2.indexOf("->");
				// 親あり
				if (idx2 != -1) {
					String title = s2.substring(0, idx2).trim();
					String parentTitle = s2.substring(idx2 + 2).trim();
					if (parentTitle.trim().isEmpty() == false) {
						// リンクは存在するがページが存在しないCategoryも存在する？
						if (mapTitleObj.get(parentTitle) != null) {
							mapTitleObj.get(title).addParent(mapTitleObj.get(parentTitle));
						} else {
							// 存在しないページ
							logger.debug("PAGE_NOT_FOUND: " + parentTitle);
							mapTitleObj.get(title).addParent(new WikiCategory(parentTitle));

						}
					}
				}
				// 親無し
				else {
//					String title = s.substring(idx+1);
				}

			}

		}

		long time2 = System.currentTimeMillis();

		logger.info("Reading done: " + (time2 - time1));

	}

}

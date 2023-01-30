package work202301;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import nlp4j.node.Node;
import nlp4j.w3c.NodeUtils;
import nlp4j.wiki.BreakException;
import nlp4j.wiki.WikiCategory;
import nlp4j.wiki.WikiDumpReader;
import nlp4j.wiki.WikiIndexItem;
import nlp4j.wiki.WikiIndexItemHandler;
import nlp4j.wiki.WikiIndexReader;
import nlp4j.wiki.WikiPage;
import nlp4j.wiki.util.MediaWikiTextUtils;

public class Work20230129CategoryPageRead {

	public static void main(String[] args) throws Exception {

		// Index File
		String indexFileName = "/usr/local/wiki/jawiki/20221101/jawiki-20221101-pages-articles-multistream-index.txt.bz2";
		// Dump File
		String dumpFileName = "/usr/local/wiki/jawiki/20221101/jawiki-20221101-pages-articles-multistream.xml.bz2";
		// Index File
		File indexFile = new File(indexFileName);
		System.err.println(indexFile.getAbsolutePath());
		// Dump File
		File dumpFile = new File(dumpFileName);
		System.err.println(dumpFile.getAbsolutePath());

		Map<String, WikiCategory> map = new HashMap<>();

		Map<String, Node<String>> nodeMap = new HashMap<>();
		Node<String> root = new Node<>("*");

		try ( //
				WikiDumpReader dumpReader = new WikiDumpReader(dumpFile, indexFile); //
		) {
			// Read Wiki Index File
			WikiIndexReader.readIndexFile(indexFile, new WikiIndexItemHandler() {
				int count = 0;

				@Override
				public void read(WikiIndexItem item) throws BreakException {

					String title = item.getTitle();

					if (title.startsWith("Category:")) {

						String title_short = title.substring(9); //

						WikiCategory cat = map.get(title_short);
						if (cat == null) {
							cat = new WikiCategory(title_short);
							map.put(title_short, cat);
						}

						Node<String> n1 = nodeMap.get(title_short);
						if (n1 == null) {
							n1 = new Node<String>(title_short);
							root.addChildNode(n1);
							nodeMap.put(title_short, n1);
						}

						count++;
//						System.out.println(item.getTitle());

						WikiPage page;
						try {
							page = dumpReader.getItem(title);
							String wikiText = page.getText();
							List<String> categories = MediaWikiTextUtils.parseCategoryTags(wikiText);
							for (String cate : categories) {
								System.out.println(title_short + " (子) -> (親) " + cate);
								{
									WikiCategory cat_parent = map.get(cate);
									if (cat_parent == null) {
										cat_parent = new WikiCategory(cate);
										map.put(cate, cat_parent);
									}
									cat_parent.addChild(cat);
									cat.setChild(true);
								}
								{
									Node<String> n2 = nodeMap.get(cate);
									if (n2 == null) {
										n2 = new Node<String>(cate);
										root.addChildNode(n2);
										nodeMap.put(cate, n2);
									}
									n1.addChildNode(n2);
								}
							}
						} catch (IOException e) {
							throw new BreakException(e);
						}
					}

//					if (count > 100) {
//						throw new BreakException();
//					}
				}
			}); // throws IOException

		}

		System.err.println("---");

		for (String key : map.keySet()) {
			WikiCategory category = map.get(key);
			if (category.isChild() == false) {
				System.err.println(category);
			}
		}

//		for (String key : nodeMap.keySet()) {
//			Node<String> n = nodeMap.get(key);
//			if (n != null) {
//				System.err.println(n.toString());
//				System.err.println(nlp4j.node.NodeUtils.toXmlString(n));
//			}
//		}

//		System.err.println(nlp4j.node.NodeUtils.toXmlString(root));

		FileUtils.write(new File("R:/xml.xml"), nlp4j.node.NodeUtils.toXmlString(root), "UTF-8", false);

	}

}

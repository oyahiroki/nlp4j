package nlp4j.yhoo_jp;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import nlp4j.KeywordWithDependency;
import nlp4j.impl.DefaultKeywordWithDependency;
import nlp4j.xml.AbstractXmlHandler;

/**
 * Yahoo! Japan Dependency AnalysisのレスポンスXMLをパースするクラスです。<br>
 * Yahoo! Japan Dependency Analysis Response XML Handler
 * 
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class YJpDaServiceResponseHandler extends AbstractXmlHandler {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	DefaultKeywordWithDependency kwd;

	HashMap<String, DefaultKeywordWithDependency> map = new HashMap<String, DefaultKeywordWithDependency>();

	ArrayList<KeywordWithDependency> roots = new ArrayList<KeywordWithDependency>();;

	int sequence = 0;

	String sentence;

	String id;

	String dependency;

	String surface;
	String reading;
	String baseform;
	String pos;
	String feature;
	int morphemID = -1;

	int maxBegin = 0;

	/**
	 * @param sentence 自然言語文字列
	 */
	public YJpDaServiceResponseHandler(String sentence) {
		super();
		this.sentence = sentence;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		logger.debug(super.getPath());
		logger.debug(super.getText());

		if ("ResultSet/Result/ChunkList/Chunk/Id".equals(super.getPath())) {
			id = super.getText();
		} //
		else if ("ResultSet/Result/ChunkList/Chunk/Dependency".equals(super.getPath())) {
			dependency = super.getText();
			logger.debug("dependency: " + dependency);
		} //
		else if ("ResultSet/Result/ChunkList/Chunk/MorphemList/Morphem/Surface".equals(super.getPath())) {
			surface = super.getText();
		} //
		else if ("ResultSet/Result/ChunkList/Chunk/MorphemList/Morphem/Reading".equals(super.getPath())) {
			reading = super.getText();
		} //
		else if ("ResultSet/Result/ChunkList/Chunk/MorphemList/Morphem/Baseform".equals(super.getPath())) {
			baseform = super.getText();
		} //
		else if ("ResultSet/Result/ChunkList/Chunk/MorphemList/Morphem/POS".equals(super.getPath())) {
			pos = super.getText();
		} //
		else if ("ResultSet/Result/ChunkList/Chunk/MorphemList/Morphem/Feature".equals(super.getPath())) {
			feature = super.getText();
			// IF(文の終わり)
		} //
			// キーワードの終わり
		else if ("ResultSet/Result/ChunkList/Chunk/MorphemList/Morphem".equals(super.getPath())) {

			this.sequence++;

			kwd = new DefaultKeywordWithDependency();
			kwd.setLex(null);
			kwd.setStr(surface);
			kwd.setFacet(pos);
			kwd.setSequence(sequence); // 連番

			{ // キーワードの開始位置・終了位置を設定
				int begin = sentence.indexOf(kwd.getStr(), maxBegin);
				if (begin != -1) {
					kwd.setBegin(begin);
					kwd.setEnd(begin + kwd.getStr().length());
					maxBegin = begin;
				}
			}

			String fullMorphemId = id + "-" + morphemID;
			map.put(fullMorphemId, kwd);

			// MEMO: ヤフーの構文解析は文をまたいでも係り受けとみなすが、修正する
			boolean isEndOfSentence = feature.startsWith("特殊,句点,");

			if (morphemID == 0) {
				if (isEndOfSentence == false) {
					kwd.setDependencyKey(this.dependency + "-" + "0");
				} else {
					kwd.setDependencyKey("-1");
				}
			} //
			else if (morphemID > 0) {
				// このMorphemが最後かもしれないのでこのようにセットしておく
				if (isEndOfSentence == false) {
					kwd.setDependencyKey(this.dependency + "-" + "0");
				} else {
					kwd.setDependencyKey("-1");
				}
				// ひとつ前の Morphem について DependencyKey を上書きする
				map.get(id + "-" + (morphemID - 1)).setDependencyKey(id + "-" + (morphemID));
			} //
			else {
			}

		} //
		else if ("ResultSet/Result/ChunkList/Chunk/MorphemList".equals(super.getPath())) {
		} //
		else if ("ResultSet/Result/ChunkList/Chunk".equals(super.getPath())) {
			morphemID = -1;
		} //
		else if ("ResultSet/Result/ChunkList".equals(super.getPath())) {
			// do nothing
		} //
		else if ("ResultSet/Result".equals(super.getPath())) {
			// do nothing
		} //
		else if ("ResultSet".equals(super.getPath())) {
			for (DefaultKeywordWithDependency kwd : map.values()) {
				logger.debug("dependencyKey: " + kwd.getDependencyKey());
				if (map.get(kwd.getDependencyKey()) != null) {
					String dependencyKey = kwd.getDependencyKey();
					logger.debug("dep: " + dependencyKey);
					kwd.setParent(map.get(dependencyKey));
				} else {
				}
			}
//			root = map.get("0-1").getRoot();

//			System.err.println("Root: " + root);

			{
				for (String key : map.keySet()) {
					DefaultKeywordWithDependency kwd = map.get(key);
					logger.debug(key + ": isRoot: " + kwd.isRoot() + " " + kwd.toString());
					if (kwd.isRoot()) {
						roots.add(kwd);
					}
				}
			}

		}

		super.endElement(uri, localName, qName);
	}

//	public KeywordWithDependency getRoot() {
//		return roots.get(0);
//	}

	/**
	 * @return キーワード
	 */
	public ArrayList<KeywordWithDependency> getRoots() {
		return this.roots;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);

		if ("ResultSet/Result/ChunkList/Chunk/MorphemList/Morphem".equals(super.getPath())) {
			morphemID++;
		} //

	}

}

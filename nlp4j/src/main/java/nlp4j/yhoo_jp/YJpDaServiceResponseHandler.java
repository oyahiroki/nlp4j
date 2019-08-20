package nlp4j.yhoo_jp;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import nlp4j.KeywordWithDependency;
import nlp4j.impl.KeywordWithDependencyImpl;
import nlp4j.xml.AbstractXmlHandler;

/**
 * Yahoo! Japan Dependency Analysis Response XML Handler
 * 
 * @author Hiroki Oya
 * @version 1.0
 *
 */
public class YJpDaServiceResponseHandler extends AbstractXmlHandler {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	KeywordWithDependencyImpl kwd;

	HashMap<String, KeywordWithDependencyImpl> map = new HashMap<String, KeywordWithDependencyImpl>();

	KeywordWithDependency root;

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
		} //
		else if ("ResultSet/Result/ChunkList/Chunk/MorphemList/Morphem".equals(super.getPath())) {
			kwd = new KeywordWithDependencyImpl();
			kwd.setLex(null);
			kwd.setStr(surface);
			kwd.setFacet("word." + pos);

			{
				int begin = sentence.indexOf(kwd.getStr(), maxBegin);
				if (begin != -1) {
					kwd.setBegin(begin);
					kwd.setEnd(begin + kwd.getStr().length());
					maxBegin = begin;
				}
			}

			String fullMorphemId = id + "-" + morphemID;
			map.put(fullMorphemId, kwd);

			if (morphemID == 0) {
				kwd.setDependencyKey(this.dependency + "-" + "0");
			} //
			else if (morphemID > 0) {
				map.get(id + "-" + (morphemID - 1)).setDependencyKey(id + "-" + (morphemID));
				kwd.setDependencyKey(this.dependency + "-" + "0");
			}

		} //
		else if ("ResultSet/Result/ChunkList/Chunk/MorphemList".equals(super.getPath())) {
		} //
		else if ("ResultSet/Result/ChunkList/Chunk".equals(super.getPath())) {
			morphemID = -1;
		} //
		else if ("ResultSet".equals(super.getPath())) {
			for (KeywordWithDependencyImpl kwd : map.values()) {
				if (map.get(kwd.getDependencyKey()) != null) {
					kwd.setParent(map.get(kwd.getDependencyKey()));
				}
			}
			root = map.get("0-1").getRoot();
		}

		super.endElement(uri, localName, qName);
	}

	public KeywordWithDependency getRoot() {
		return root;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);

		if ("ResultSet/Result/ChunkList/Chunk/MorphemList/Morphem".equals(super.getPath())) {
			morphemID++;
		} //

	}

}

package nlp4j.annotator;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.FieldAnnotator;
import nlp4j.Keyword;
import nlp4j.impl.DefaultKeyword;
import nlp4j.util.CharacterUtils;
import nlp4j.util.StringUtils;

/**
 * @author Hiroki Oya
 * @since 1.3.1.0
 *
 */
public class EmojiAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator, FieldAnnotator {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public void setProperty(String key, String value) {
		super.setProperty(key, value);
	}

	@Override
	public void annotate(Document doc) throws Exception {

		for (String target : super.targets) {
			String text = doc.getAttributeAsString(target);
			if (text == null || text.trim().isEmpty()) {
				continue;
			} //
			else {
				int[] cpa = StringUtils.toCodePointArray(text);
				for (int n = 0; n < cpa.length; n++) {

					int cp = cpa[n];

					String emojiChar = CharacterUtils.toChar(cp);
					String name = CharacterUtils.getName(cp);
					if (name != null) {
						name = name.replace(" ", "_");
					}
					String block = CharacterUtils.getUnicodeBlock(cp);

					if (block == null //
							|| name == null //
							|| block.startsWith("BASIC_LATIN") //
							|| block.startsWith("KATAKANA") //
							|| block.startsWith("HIRAGANA") //
							|| block.startsWith("CJK") //
							|| name.contains("LATIN") //
							|| name.equals("HORIZONTAL_ELLIPSIS") //
							|| name.equals("VARIATION_SELECTOR-16") //
							|| name.equals("FULLWIDTH_LEFT_PARENTHESIS") //
							|| name.equals("FULLWIDTH_RIGHT_PARENTHESIS") //
							|| name.contains("KATAKANA") //
							|| name.contains("HIRAGANA") //
							|| name.startsWith("VARIATION_SELECTOR") //

							|| (block.equals("EMOTICONS") == false
									&& block.equals("MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS") == false) //

					) {
						continue;
					}

					logger.info("block=" + block + ",emoji=" + name);
					{
						Keyword kwd = new DefaultKeyword();
						kwd.setBegin(n);
						kwd.setEnd(n + 1);
						kwd.setLex(name);
						kwd.setStr(name);
						kwd.setFacet("emoji");
						doc.addKeyword(kwd);
					}
					{
						Keyword kwd = new DefaultKeyword();
						kwd.setBegin(n);
						kwd.setEnd(n + 1);
						kwd.setLex(block);
						kwd.setStr(block);
						kwd.setFacet("emojiblock");
						doc.addKeyword(kwd);
					}

					if (emojiChar != null) {
						Keyword kwd = new DefaultKeyword();
						kwd.setBegin(n);
						kwd.setEnd(n + 1);
						kwd.setLex(emojiChar);
						kwd.setStr(emojiChar);
						kwd.setFacet("emojichar");
						doc.addKeyword(kwd);
					}

				}

			}

		}

	}

}

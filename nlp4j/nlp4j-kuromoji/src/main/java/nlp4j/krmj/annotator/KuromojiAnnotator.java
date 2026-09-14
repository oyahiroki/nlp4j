package nlp4j.krmj.annotator;

import java.io.StringReader;
import java.lang.invoke.MethodHandles;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.ja.JapaneseTokenizer;
import org.apache.lucene.analysis.ja.JapaneseTokenizer.Mode;
import org.apache.lucene.analysis.ja.tokenattributes.BaseFormAttribute;
import org.apache.lucene.analysis.ja.tokenattributes.PartOfSpeechAttribute;
import org.apache.lucene.analysis.ja.tokenattributes.ReadingAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

import nlp4j.AbstractDocumentAnnotator;
import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.impl.DefaultKeyword;

/**
 * Kuromoji Annotator (Lucene Kuromoji 実装)
 *
 * <p>Apache Lucene の JapaneseTokenizer を使用する。
 * Lucene の辞書は JVM 内でシングルトンキャッシュされるため、
 * インスタンスを複数生成しても辞書ロードコストは初回のみ発生する。
 *
 * @author Hiroki Oya
 * @since 1.2
 */
public class KuromojiAnnotator extends AbstractDocumentAnnotator implements DocumentAnnotator {

	static private final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/** 英字のみの文字列にマッチするパターン（毎回 compile しないよう定数化） */
	private static final Pattern ALPHA_PATTERN = Pattern.compile("[a-zA-Z]+");

	@Override
	public void annotate(Document doc) throws Exception {

		logger.debug("processing document ... ");
		long time1 = System.currentTimeMillis();

		for (String target : targets) {
			Object obj = doc.getAttribute(target);
			if (obj == null || !(obj instanceof String)) {
				continue;
			}

			String text = (String) obj;

			// JapaneseTokenizer は使い捨て（reset → incrementToken → end → close）
			// Lucene の辞書は SingletonHolder でキャッシュされるため初期化コストは初回のみ
			try (JapaneseTokenizer tokenizer = new JapaneseTokenizer(null, false, Mode.NORMAL)) {
				tokenizer.setReader(new StringReader(text));
				tokenizer.reset();

				CharTermAttribute    termAttr = tokenizer.addAttribute(CharTermAttribute.class);
				OffsetAttribute    offsetAttr = tokenizer.addAttribute(OffsetAttribute.class);
				BaseFormAttribute    baseAttr = tokenizer.addAttribute(BaseFormAttribute.class);
				PartOfSpeechAttribute posAttr = tokenizer.addAttribute(PartOfSpeechAttribute.class);
				ReadingAttribute    readAttr  = tokenizer.addAttribute(ReadingAttribute.class);

				int sequence = 1;

				while (tokenizer.incrementToken()) {

					String surface  = termAttr.toString();
					String baseForm = baseAttr.getBaseForm();   // null の場合あり
					String reading  = readAttr.getReading();    // null の場合あり
					String pos      = posAttr.getPartOfSpeech(); // "名詞-代名詞-一般" 形式
					int    begin    = offsetAttr.startOffset();
					int    end      = offsetAttr.endOffset();

					logger.debug("{} {} {} {}", surface, baseForm, reading, pos);

					DefaultKeyword kwd = new DefaultKeyword();

					// baseForm が null（未知語等）の場合は表層形で代替
					kwd.setLex(baseForm != null ? baseForm : surface);
					kwd.setStr(surface);
					// reading が null の場合は "*" で代替（既存の英字補完ロジックと整合）
					kwd.setReading(reading != null ? reading : "*");

					// 英字トークン：lex が表層形と異なる or baseForm が null の場合に補完
					// @since 1.2.0.1
					if ("*".equals(kwd.getLex()) && "*".equals(kwd.getReading())
							&& ALPHA_PATTERN.matcher(kwd.getStr()).matches()) {
						kwd.setLex(kwd.getStr());
					}

					kwd.setBegin(begin);
					kwd.setEnd(end);

					// 品詞は "-" 区切りの先頭要素（例: "名詞-代名詞-一般" → "名詞"）
					if (pos != null) {
						int dash = pos.indexOf('-');
						kwd.setFacet(dash >= 0 ? pos.substring(0, dash) : pos);
					}

					kwd.setSequence(sequence);
					doc.addKeyword(kwd);
					sequence++;
				}

				tokenizer.end();
			}
		}

		long time2 = System.currentTimeMillis();
		logger.debug("processing document ... done " + (time2 - time1));
	}
}

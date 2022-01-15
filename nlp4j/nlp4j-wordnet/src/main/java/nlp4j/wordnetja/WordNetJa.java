package nlp4j.wordnetja;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.SAXParser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.xml.SAXParserUtils;

/**
 * <pre>
 * created_at 2021-12-05 18:37
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class WordNetJa {

	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	Map<String, LexicalEntry> writtenFormPosLexicalEntryMap;
	Map<String, List<String>> writtenFormPosMap;

	WordNetJaXmlHandler handler = new WordNetJaXmlHandler();

	private List<String> WrittenFormList;

	/**
	 * Read LMF
	 * 
	 * @param lmf_xml_gz_file
	 * @throws IOException
	 */
	public WordNetJa(File lmf_xml_gz_file) throws IOException {
		InputStream is = new FileInputStream(lmf_xml_gz_file); //
		readLmf(is);
	}

	/**
	 * Read LMF
	 * 
	 * @param is
	 * @throws IOException
	 */
	public WordNetJa(InputStream is) throws IOException {
		readLmf(is);
	}

	public List<String> find(String word, String filter) {

		List<String> ss = new ArrayList<>();

		List<LexicalEntry> ee = findLexicalEntry(word, filter);

		for (LexicalEntry et : ee) {
			ss.add(et.getLemmaWrittenForm());
		}

		return ss;

	}

	public List<String> findHypernyms(String word) {
		String filter = "hype";
		return find(word, filter);
	}

	public List<String> findHyponyms(String word) {
		String filter = "hypo";
		return find(word, filter);
	}

//	public List<Synset> getSynsets(String writtenForm) {
//		return null;
//	}

	public List<LexicalEntry> findLexicalEntry(String word, String filter) {

		List<LexicalEntry> list = new ArrayList<>();

		Map<String, List<String>> writtenFormPosMap = handler.getWrittenFormPosMap();
		Map<String, LexicalEntry> writtenFormPosLexicalEntryMap = handler.getWrittenFormPosLexicalEntryMap();

		if (writtenFormPosMap.get(word) == null) {
			return list;
		}

		for (String pos : writtenFormPosMap.get(word)) {

			LexicalEntry entry = writtenFormPosLexicalEntryMap.get(word + "." + pos);

			for (String synset_id : entry.getSynset_ids()) {
				Synset synset = handler.getSynsetIdObjMap().get(synset_id);

				for (SynsetRelation r : synset.getSynsetRelations()) {
					if (filter != null && filter.equals(r.getRelType()) == false) {
						continue;
					}
//					System.err.println("\t" + r.getRelType());
//					System.err.println("\t" + r.getTargets());
					String targets = r.getTargets();
//					System.err.println("\t\t" + handler.getSynsetLexicalEntryMap().get(targets));

					if (handler.getSynsetLexicalEntryMap().containsKey(targets)) {
						for (LexicalEntry ent : handler.getSynsetLexicalEntryMap().get(targets)) {
							list.add(ent);
//							ss.add(ent.getLemmaWrittenForm());
						}
					}
				}
			}
		}
		return list;
	}

	public LexicalEntry getEntry(String writtenForm, String pos) {
		return this.writtenFormPosLexicalEntryMap.get(writtenForm + "." + pos);
	}

	public List<LexicalEntry> getLexicalEntries() {
		return handler.getLexicalEntries();
	}

	public List<LexicalEntry> getLexicalEntriesInSameSynset(LexicalEntry entry) {
		List<LexicalEntry> list = new ArrayList<LexicalEntry>();
		List<String> synset_ids = entry.getSynset_ids();
		for (String synset_id : synset_ids) {
			List<LexicalEntry> les = handler.getSynsetLexicalEntryMap().get(synset_id);
			list.addAll(les);
		}
		return list;
	}

	/**
	 * 同じSynsetに属するLexicalEntryを返す
	 * 
	 * @param word
	 * @return
	 */
	public List<LexicalEntry> getLexicalEntriesInSameSynset(String word) {
		List<LexicalEntry> list = new ArrayList<LexicalEntry>();
		// word -> lexical entry
		List<String> poss = getPos(word);
		if (poss == null) {
			return list;
		}
		for (String pos : poss) {
			LexicalEntry entry = getEntry(word, pos);
			List<String> synset_ids = entry.getSynset_ids();
			for (String synset_id : synset_ids) {
//				System.err.println("# " + handler.getSynsetIdObjMap().get(synset_id).getDefinition_gloss());
				List<LexicalEntry> les = handler.getSynsetLexicalEntryMap().get(synset_id);
				for (LexicalEntry le : les) {
//					System.err.println(le.getLemmaWrittenForm());
					list.add(le);
				}
			}
		}
		return list;
	}

	public List<Synset> getLinkedSynsets(LexicalEntry entry) {
		List<Synset> synsets = new ArrayList<>();
		Map<String, Synset> synsetMap = handler.getSynsetIdObjMap();
		for (String synsetId : entry.getSynset_ids()) {
			if (synsetMap.containsKey(synsetId) == true) {
				synsets.add(synsetMap.get(synsetId));
			}
		}
		return synsets;
	}

	/**
	 * <pre>
	 * 語の品詞を返す
	 * 「学校」→「n」
	 * </pre>
	 * 
	 * @param string :
	 * @return
	 */
	public List<String> getPos(String string) {
		return this.writtenFormPosMap.get(string);
	}

	/**
	 * <pre>
	 * created at 2022-01-12
	 * </pre>
	 * 
	 * @return 見出しのリスト
	 */
	public List<String> getWrittenFormList() {
		return WrittenFormList;
	}

	/**
	 * LMLファイルを読み込む
	 * 
	 * @param is
	 * @throws IOException
	 */
	private void readLmf(InputStream is) throws IOException {
		logger.info("loading: start");
		long time1 = System.currentTimeMillis();
		try (//
				GZIPInputStream gis = new GZIPInputStream(is);//
		) {
			SAXParser saxParser = SAXParserUtils.getNewSAXParser();
			saxParser.parse(gis, handler);
			this.WrittenFormList = handler.getWrittenFormList();
			this.writtenFormPosLexicalEntryMap = handler.getWrittenFormPosLexicalEntryMap();
			this.writtenFormPosMap = handler.getWrittenFormPosMap();
		} catch (Exception e) {
			throw new IOException(e);
		}
		long time2 = System.currentTimeMillis();
		logger.info("loading: done");
		logger.info("load time: " + (time2 - time1));
	}

	public List<LexicalEntry> searchEntries(String word) {
		List<LexicalEntry> list = new ArrayList<>();
		return list;
	}

	public List<LexicalEntry> searchEntriesByRegex(String regex) {
		List<LexicalEntry> list = new ArrayList<>();
		return list;
	}

}

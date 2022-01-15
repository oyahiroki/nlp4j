package nlp4j.wordnetja;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import nlp4j.Keyword;
import nlp4j.impl.DefaultKeyword;
import nlp4j.xml.AbstractXmlHandler;

/**
 * <pre>
 * created_at 2021-12-05 16:24
 * </pre>
 * 
 * @author Hiroki Oya
 *
 */
public class WordNetJaXmlHandler extends AbstractXmlHandler {

	private HashMap<String, List<String>> writtenFormSynsetMap = new HashMap<>();
//	HashMap<String, List<String>> synsetWrittenFormMap = new HashMap<>();
//	HashMap<String, List<String>> synsetRelationMap = new HashMap<>();
//
//	String writtenForm;
	private String synsetId;
//	private Synset synset;

	private HashMap<String, Synset> synsetIdObjMap = new HashMap<>();

//	ArrayList<Synset> synsets = new ArrayList<>();

	public Map<String, Synset> getSynsetIdObjMap() {
		return synsetIdObjMap;
	}

	// 現在読み込み中のオブジェクト
	private LexicalEntry lexicalEntry;

	// LexicalEntry のリスト
	private List<LexicalEntry> lexicalEntries = new ArrayList<>();

	/**
	 * @return LexicalEntry のリスト
	 */
	public List<LexicalEntry> getLexicalEntries() {
		return lexicalEntries;
	}

	private String writtenForm;

	// id -> LexicalEntry
	private Map<String, LexicalEntry> lexicalEntryIdMap = new HashMap<>();

	// "均質化.n" -> LexicalEntryObject
	private Map<String, LexicalEntry> writtenFormPosLexicalEntryMap = new HashMap<>();

	private Map<String, List<LexicalEntry>> synsetLexicalEntryMap = new HashMap<>();

	public Map<String, List<LexicalEntry>> getSynsetLexicalEntryMap() {
		return synsetLexicalEntryMap;
	}

	public Map<String, LexicalEntry> getWrittenFormPosLexicalEntryMap() {
		return writtenFormPosLexicalEntryMap;
	}

	// writtenForm(見出し)のリスト
	private List<String> writtenFormList = new ArrayList<String>();

	// // "均質化" -> "n","v"
	private HashMap<String, List<String>> writtenFormPosMap = new HashMap<>();

	private Synset synsetObj;
	private List<Synset> synsets = new ArrayList<>();

	public Map<String, List<String>> getWrittenFormPosMap() {
		return this.writtenFormPosMap;
	}

//	@Override
//	public void endElement(String uri, String localName, String qName) throws SAXException {
////		System.err.println(super.getPath());
//		super.endElement(uri, localName, qName);
//	}

	public Map<String, LexicalEntry> getLexicalEntryIdMap() {
		return lexicalEntryIdMap;
	}

//	public Map<String, LexicalEntry> getLexicalEntryWrittenFormMap() {
//		return lexicalEntryWrittenFormMap;
//	}

//	public Map<String, LexicalEntry> getLexicalEntryWrittenFormPosMap() {
//		return lexicalEntryWrittenFormPosMap;
//	}

//	public Map<String, LexicalEntry> getSynsetLexicalEntryMap() {
//		return synsetLexicalEntryMap;
//	}

//	public HashMap<String, Synset> getSynsetMap() {
//		return synsetMap;
//	}

//	public HashMap<String, List<String>> getSynsetRelationMap() {
//		return synsetRelationMap;
//	}

//	public HashMap<String, List<String>> getSynsetWrittenFormMap() {
//		return synsetWrittenFormMap;
//	}

//	public HashMap<String, List<String>> getWrittenFormSynsetMap() {
//		return writtenFormSynsetMap;
//	}

	public List<String> getWrittenFormList() {
		return writtenFormList;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		super.startElement(uri, localName, qName, attributes);

		String p = super.getPath();

//		System.err.println(super.getPath());

		// LexicalEntry 語の定義
		// Lexicon/LexicalEntry
		// Lexicon/LexicalEntry/Lemma
		// Lexicon/LexicalEntry/Sense

		// 学校 -> jpn-1.1-08276720-n, jpn-1.1-15203229-n
		// jpn-1.1-08276720-n -> 教育機関
		// jpn-1.1-15203229-n -> 学校の授業期間。学校で授業が行われる期間。
		// hype : 上位語
		// hypo : 下位語
		//

//		   <LexicalEntry id ='w195566'>
//		      <Lemma writtenForm='学校' partOfSpeech='n'/>
//		      <Sense id='w195566_08276720-n' synset='jpn-1.1-08276720-n'/>
//		      <Sense id='w195566_15203229-n' synset='jpn-1.1-15203229-n'/>
//		   </LexicalEntry>

//		   <Synset id='jpn-1.1-08276720-n' baseConcept='3'>
//			<Definition gloss="教育機関。">
//		   	<Statement example="その学校は1900年に創立された"/>
//			</Definition>
//		      <SynsetRelations>
//		         <SynsetRelation targets='jpn-1.1-08287586-n' relType='mmem'/>
//		         <SynsetRelation targets='jpn-1.1-10560352-n' relType='mmem'/>
//		         <SynsetRelation targets='jpn-1.1-08276342-n' relType='hype'/>
//		         <SynsetRelation targets='jpn-1.1-08278589-n' relType='hypo'/>
//		         <SynsetRelation targets='jpn-1.1-08277805-n' relType='hypo'/>
//               ...
//		         <SynsetRelation targets='jpn-1.1-08286039-n' relType='hypo'/>
//		      </SynsetRelations>
//		   </Synset>

		if (p.equals("LexicalResource/Lexicon/LexicalEntry")) {
			// id is like 'w195566'
			String id = attributes.getValue("id");
			this.lexicalEntry = new LexicalEntry();
			{
				this.lexicalEntry.setId(id); // id is like 'w195566'
			}
			this.lexicalEntries.add(this.lexicalEntry);
			this.lexicalEntryIdMap.put(id, lexicalEntry);
		} //
		else if (p.equals("LexicalResource/Lexicon/LexicalEntry/Lemma")) {
			// writtenForm is like '学校'
			String writtenForm = attributes.getValue("writtenForm");
			this.writtenForm = writtenForm;
			{
				if (this.writtenFormList.contains(writtenForm) == false) {
					this.writtenFormList.add(writtenForm);
				}
			}
			// pos is like 'n'
			String pos = attributes.getValue("partOfSpeech");
			this.lexicalEntry.setLemmaWrittenForm(writtenForm);
			this.lexicalEntry.setPos(pos);

			// "均質化.n" -> LexicalEntryObject
			writtenFormPosLexicalEntryMap.put(writtenForm + "." + pos, this.lexicalEntry);

			// some writtenForm is mapped to multiple POSs
			// "均質化" -> "n","v"
			if (this.writtenFormPosMap.containsKey(writtenForm)) {
				this.writtenFormPosMap.get(writtenForm).add(pos);
			} else {
				List<String> list = new ArrayList<>();
				list.add(pos);
				this.writtenFormPosMap.put(writtenForm, list);
			}
		} //
		else if (p.equals("LexicalResource/Lexicon/LexicalEntry/Sense")) {
			// id is like 'w195566_08276720-n'
			String id = attributes.getValue("id");
			// synset is like 'jpn-1.1-08276720-n'
			String synset = attributes.getValue("synset");

			if (this.writtenFormSynsetMap.containsKey(this.writtenForm) == false) {
				this.writtenFormSynsetMap.put(this.writtenForm, new ArrayList<String>());
			}
			this.writtenFormSynsetMap.get(writtenForm).add(synset);

//			if (this.synsetWrittenFormMap.get(synset) == null) {
//				this.synsetWrittenFormMap.put(synset, new ArrayList<String>());
//			}
//			this.synsetWrittenFormMap.get(synset).add(writtenForm);

			this.lexicalEntry.addSynsetId(synset);

			if (this.synsetLexicalEntryMap.containsKey(synset) == false) {
				this.synsetLexicalEntryMap.put(synset, new ArrayList<LexicalEntry>());
			}

			this.synsetLexicalEntryMap.get(synset).add(lexicalEntry);

		} //

		// Synset 同義語グループの定義
		// id='jpn-1.1-01503061-n'
		// baseConcept
		else if (p.equals("LexicalResource/Lexicon/Synset")) {
			String synsetId = attributes.getValue("id");
			int baseConcept = Integer.parseInt(attributes.getValue("baseConcept"));

			this.synsetId = synsetId;
			this.synsetObj = new Synset();
			{
				this.synsetObj.setId(attributes.getValue("id"));
				this.synsetObj.setBaseConcept(baseConcept);
			}

//			this.synsetRelationMap.put(synsetId, new ArrayList<String>());

			this.synsets.add(this.synsetObj);
			this.synsetIdObjMap.put(synsetId, synsetObj);

		} //
			// gloss
			// LexicalResource/Lexicon/Synset/Definition
		else if (p.equals("LexicalResource/Lexicon/Synset/Definition")) {
			this.synsetObj.setDefinition_gloss(attributes.getValue("gloss"));
		} //
			// LexicalResource/Lexicon/Synset/Definition/Statement
		else if (p.equals("LexicalResource/Lexicon/Synset/Definition/Statement")) {
			this.synsetObj.setStatement_example(attributes.getValue("example"));
		} //
			// LexicalResource/Lexicon/Synset/SynsetRelations/SynsetRelation
		else if (p.equals("LexicalResource/Lexicon/Synset/SynsetRelations/SynsetRelation")) {
			String targets = attributes.getValue("targets");
			String relType = attributes.getValue("relType");
			SynsetRelation sr = new SynsetRelation();
			{
				sr.setTargets(targets);
				sr.setRelType(relType);
			}
			this.synsetObj.addSynsetRelations(sr);
		} //

	}
}

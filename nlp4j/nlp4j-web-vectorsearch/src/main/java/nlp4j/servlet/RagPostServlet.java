package nlp4j.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.annotator.KeywordFacetMappingAnnotator;
import nlp4j.krmj.annotator.KuromojiAnnotator;
import nlp4j.llm.embeddings.EmbeddingAnnotator;
import nlp4j.servlet.util.ServletUtils;
import nlp4j.solr.importer.SolrDocumentImporterVector;
import nlp4j.util.DateUtils;

/**
 * ベクトルDBに文書をポストする
 */
public class RagPostServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RagPostServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/javascript; charset=utf-8");
		response.setCharacterEncoding("UTF-8");

		String solr_endPoint = Config.SOLR_ENDPOINT;
		String solr_collection = Config.SOLR_COLLECTION;

		try (PrintWriter pw = response.getWriter();) {

			try {
				Document doc = ServletUtils.parse(request);
				{
					doc.setId(DateUtils.get_yyyyMMdd_HHmmss()); // 文書ID
				}
				logger.info("annotating ...");
				{
					{ // Kuromoji
						KuromojiAnnotator ann = new KuromojiAnnotator();
						ann.setProperty("target", "text");
						ann.annotate(doc); // throws Exception
					}
					{ // Keyword Facet Mapping for Solr
						KeywordFacetMappingAnnotator ann = new KeywordFacetMappingAnnotator();
						ann.setProperty("mapping", KeywordFacetMappingAnnotator.DEFAULT_MAPPING);
						ann.annotate(doc); // throws Exception
					}
					{ // Embedding
						DocumentAnnotator ann = new EmbeddingAnnotator();
						ann.setProperty("target", "text");
						ann.annotate(doc); // throws Exception
					}
				}
				logger.info("annotating ... done");

				logger.info("importing ...");
				{ // IMPORT DOCUMENT
					SolrDocumentImporterVector importer = new SolrDocumentImporterVector();
					{
						importer.setProperty("endPoint", solr_endPoint);
						importer.setProperty("collection", solr_collection);
						importer.setProperty("keyword_facet_field_mapping", //
								"word->word_ss" //
						);
						importer.setProperty("attribute_field_mapping", //
								"text->text_txt_ja" //
						);
					}
					importer.importDocument(doc); // throws IOException

					importer.commit(); // throws IOException
					importer.close();
				}
				logger.info("importing ... done");

			} catch (Exception e) {
				response.setStatus(500);
				return;
			}

			response.setStatus(200);
			pw.println("{'message':'OK','timestamp':'" + "" + DateUtils.get_yyyyMMdd_HHmmss() + "'}");
			pw.flush();

		} // END_OF_TRY

	} // END_OF_POST_METHOD

} // END_OF_CLASS

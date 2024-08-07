package nlp4j.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.annotator.KeywordFacetMappingAnnotator;
import nlp4j.krmj.annotator.KuromojiAnnotator;
import nlp4j.llm.embeddings.EmbeddingAnnotator;
import nlp4j.solr.importer.SolrDocumentImporterVector;
import nlp4j.util.DateUtils;

/**
 * Servlet implementation class NlpServlet
 */
public class RagPostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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

		try (PrintWriter pw = response.getWriter();) {

			try {
				Document doc = ServletUtils.parse(request);

				doc.setId(DateUtils.get_yyyyMMdd());

				String endPoint = "http://localhost:8983/solr/";
				String collection = "sandbox";

				try {
					{ // Kuromoji
						KuromojiAnnotator ann = new KuromojiAnnotator();
						ann.setProperty("target", "text");
						ann.annotate(doc);
					}
					{ // Keyword Facet Mapping for Solr
						KeywordFacetMappingAnnotator ann = new KeywordFacetMappingAnnotator();
						ann.setProperty("mapping", KeywordFacetMappingAnnotator.DEFAULT_MAPPING);
						ann.annotate(doc);
					}
					{
						DocumentAnnotator ann = new EmbeddingAnnotator();
						ann.setProperty("target", "text");
						ann.annotate(doc);
						System.err.println(doc.getAttributeAsListNumbers("vector").size());

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				{ // IMPORT DOCUMENT
					SolrDocumentImporterVector importer = new SolrDocumentImporterVector();
					{
						importer.setProperty("endPoint", endPoint);
						importer.setProperty("collection", collection);
						importer.setProperty("keyword_facet_field_mapping", //
								"word->word_ss" //
						);
						importer.setProperty("attribute_field_mapping", //
								"text->text_txt_ja" //
						);
					}
					importer.importDocument(doc);

					importer.commit();
					importer.close();
				}

			} catch (IOException e) {
				response.setStatus(400); // Bad Requiest
				return;
			}

			{
//				DocumentAnnotator annotator = new YJpMaAnnotator();
//				try {
//					annotator.annotate(doc);
//				} catch (Exception e) {
//					e.printStackTrace();
//					response.setStatus(500); // Server Error
//					response.getWriter().println("Server Error");
//					return;
//				}
			}

//			response.setContentType("application/javascript; charset=utf-8");
			response.setStatus(200);

			pw.println("{'message':'OK','timestamp':'" + "" + DateUtils.get_yyyyMMdd_HHmmss() + "'}");
			pw.flush();

//			response.getWriter().println(JsonUtils.prettyPrint(DocumentUtil.toJsonString(doc)));

		}

	}

}

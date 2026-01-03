package nlp4j.servlet;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.krmj.annotator.KuromojiAnnotator;
import nlp4j.okt.annotator.OktAnnotator;
import nlp4j.util.DocumentUtil;
import nlp4j.util.JsonObjectUtils;
import nlp4j.util.JsonUtils;
import nlp4j.yhoo_jp.YJpMaAnnotator;

/**
 * Servlet implementation class NlpServlet
 */
public class NlpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NlpServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// gson
		Gson gson = new Gson();
		// request body json
		JsonObject jo = null;

		Document doc = null;
		
		String lang = null;

		String contentType = request.getContentType();

		System.err.println(contentType);

		// if(request body is JSON)
		if (contentType.equals("text/json") //
				|| contentType.equals("application/json") //
				|| contentType.startsWith("text/json") //
				|| contentType.startsWith("application/json")) {
			String json = request.getReader().lines().collect(Collectors.joining("\r\n"));
			System.err.println("DEBUG: " + json);
			// JSON -> Document
			try {
				doc = DocumentUtil.parseFromJson(json);
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(400); // Bad Requiest
				response.getWriter().println("Invalid Json");
				return;
			}
			jo = JsonObjectUtils.fromJson(json);
		}
		
		if(jo != null) {
			if(jo.get("lang") != null) {
				lang = jo.get("lang").getAsString();
			}
		}

		if (doc == null) {
			response.setStatus(400); // Bad Requiest
			return;
		}

//		{ // YJpMaAnnotator
//			DocumentAnnotator annotator = new YJpMaAnnotator();
//			annotator.setProperty("target", "text");
//			try {
//				annotator.annotate(doc);
//			} catch (Exception e) {
//				e.printStackTrace();
//				response.setStatus(500); // Server Error
//				response.getWriter().println("Server Error");
//				return;
//			}
//		}
		
		if(lang == null || "ja".equals(lang)){
			{ // Kuromoji
				DocumentAnnotator annotator = new KuromojiAnnotator();
				annotator.setProperty("target", "text");
				try {
					annotator.annotate(doc);
				} catch (Exception e) {
					e.printStackTrace();
					response.setStatus(500); // Server Error
					response.getWriter().println("Server Error");
					return;
				}
			}
		}
		
		else if("ko".equals(lang)){
			
			{ // Kuromoji
				DocumentAnnotator annotator = new OktAnnotator();
				annotator.setProperty("target", "text");
				try {
					annotator.annotate(doc);
				} catch (Exception e) {
					e.printStackTrace();
					response.setStatus(500); // Server Error
					response.getWriter().println("Server Error");
					return;
				}
			}
		}

		response.setContentType("application/javascript; charset=utf-8");
		response.setStatus(200);

		response.getWriter().println(JsonUtils.prettyPrint(DocumentUtil.toJsonString(doc)));
		response.getWriter().close();
	}

}

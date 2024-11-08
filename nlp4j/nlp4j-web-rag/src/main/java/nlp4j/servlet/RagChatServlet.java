package nlp4j.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.impl.BaseHttpSolrClient.RemoteSolrException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.DocumentAnnotator;
import nlp4j.DocumentBuilder;
import nlp4j.llm.embeddings.EmbeddingAnnotator;
import nlp4j.openai.Config;
import nlp4j.openai.OpenAI;
import nlp4j.openai.OpenAIEmbeddingAnnotator;
import nlp4j.servlet.util.ServletStreamUtils;
import nlp4j.solr.search.SolrSearchClient;
import nlp4j.util.JsonUtils;
import nlp4j.web.rag.ChatHistory;

/**
 * RagChat: ベクトル検索の結果を会話に反映する
 */
public class RagChatServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RagChatServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		// チャット履歴
		ChatHistory history = (ChatHistory) session.getAttribute("chathistory");
		if (history == null) {
			history = new ChatHistory();
			session.setAttribute("chathistory", history);
		}

		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");

		logger.info("processing ...");

		String q = request.getParameter("q");

		// ベクトル検索結果
		// [{'type':'user_knowledge_base','text':'検索結果テキスト',score:0.9,'description':'this
		// data is from user's knowledge database'} ... ]
		JsonArray docs_knowledge = new JsonArray();

		try (PrintWriter pw = response.getWriter();) {

			{
				try { // ベクトル検索

					// Send message
					ServletStreamUtils.printMessageStream(pw, "embedding ...");

					Document doc = (new DocumentBuilder()).text(q).build();
					
					{ // Embedding (1)
//						DocumentAnnotator ann = new EmbeddingAnnotator();
//						ann.setProperty("target", "text");
//						ann.annotate(doc);
//						logger.info("vector.size: " + doc.getAttributeAsListNumbers("vector").size());
					}
					{ // Embedding (2)
						OpenAIEmbeddingAnnotator ann = new OpenAIEmbeddingAnnotator();
						ann.setProperty("target", "text");
						ann.annotate(doc);
						ann.close();
						logger.info("vector.size: " + doc.getAttributeAsListNumbers("vector").size());
					}
					
					ServletStreamUtils.printMessageStream(pw, "embedding ... done");

					final String solr_endPoint = nlp4j.servlet.Config.SOLR_ENDPOINT;
					final String solr_collection = nlp4j.servlet.Config.SOLR_COLLECTION;

					{ // Solr Vector Search
						try (SolrSearchClient client = new SolrSearchClient.Builder(solr_endPoint).build()) {
							// GET Search Response as JsonObject
							JsonObject responseObject = client.searchByVector(solr_collection, doc);
							removeVector(responseObject);

							{ // ベクトル検索結果を知識として追加する ...
								JsonArray response_dd = responseObject //
										.get("response").getAsJsonObject() //
										.get("docs").getAsJsonArray();
								// FOR_EACH(RESPONSE_DOCUMENT)
								for (int n = 0; n < response_dd.size(); n++) {
									JsonObject d = response_dd.get(n).getAsJsonObject();
									JsonObject k = new JsonObject(); // knowledge
									{
										k.addProperty("type", "user_knowledge_base");
										k.addProperty("text", d.get("text_txt_ja").getAsString()); // テキスト
										k.addProperty("score", d.get("score").getAsNumber()); // スコア
										k.addProperty("description", "this data is from user's knowledge database");
									}
									docs_knowledge.add(k);
								} // END_OF_FOREACH
							} // ... ベクトル検索結果を知識として追加する
						} catch (IOException | RemoteSolrException e) {
							System.err.println("SKIP THIS TEST: " + e.getMessage());
						}
					} // END_OF(Solr Vector Search)
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			{ // 文書検索結果を返す
				JsonObject jo_documents = new JsonObject();
				{
					jo_documents.addProperty("type", "documents");
					jo_documents.add("data", docs_knowledge);
				}
				pw.write("data: " + jo_documents.toString() + "\n\n");
				pw.flush();
			}

			final String model = "gpt-4";
			
			
			
			
			
			
			
			
			JsonObject question_from_user = new JsonObject();
			{
				question_from_user.addProperty("name", "question from end user");
				question_from_user.addProperty("value", q);
			}
			JsonObject knowledge_base = new JsonObject();
			{
				knowledge_base.addProperty("name", "Knowledge base for answering");
				knowledge_base.add("value", docs_knowledge);
			}

			String content //
					= "{name:'question from end user'}は一般ユーザーからの質問です。必ず日本語で回答してください。\n" //
							+ "{name:'Knowledge base for answering'}は回答のための知識ベースです。" //
							+ "{name:'chat history'}は、今回の会話の履歴です。\n" //
							+ "会話の履歴と知識ベースを用いて回答してください。\n" //
							+ "---\n" //
							+ question_from_user.toString() + "\n"// question
							+ knowledge_base.toString() + "\n" // knowledge base
							+ history.toString() + "\n" //
			;

			// Open AI API へのリクエスト
			JsonObject requestBody = new JsonObject();
			{
				requestBody.addProperty("model", model);
				{
					JsonArray messages = new JsonArray();
					{
						JsonObject message = new JsonObject();
						message.addProperty("role", "user");
						message.addProperty("content", content);
						messages.add(message);
					}
					requestBody.add("messages", messages);
				}
				requestBody.addProperty("stream", true);
			}

			logger.info("request to API:\n" + JsonUtils.prettyPrint(requestBody));

			try (OpenAI openai = new OpenAI(new Config());) {

				try (BufferedReader br = new BufferedReader(
						new InputStreamReader(openai.chat_completions_stream(requestBody)))) {

					String line;
					StringBuilder sb_response = new StringBuilder();

					while ((line = br.readLine()) != null) {
						// Send each chunk of data as an SSE (Server-Sent Event)
						if (logger.isDebugEnabled()) {
							logger.debug("line.length: " + line.length());
							logger.debug("line: " + line);
						}
						if (line.trim().isEmpty()) {
							continue;
						}
						if ("data: [DONE]".equals(line)) {
							logger.info("done response from Open AI API");
							break;
						}
						{
							JsonObject r = (new Gson()).fromJson(line.substring("data: ".length()), JsonObject.class);
							{
								JsonElement je_content = r //
										.get("choices").getAsJsonArray() //
										.get(0).getAsJsonObject() //
										.get("delta").getAsJsonObject().get("content") //
								;
								if (je_content == null) {
									continue;
								}
								String s = je_content.getAsString();
								sb_response.append(s);
							}
						}
						{ // STREAM_RESPONSE
							JsonObject jo_stream = new JsonObject();
							{
								jo_stream.addProperty("type", "openai.stream");
								jo_stream.add("data", (new Gson()).fromJson(line.substring(6), JsonObject.class));
							}
							pw.write("data: " + jo_stream.toString() + "\n\n");
							pw.flush();
						}
					} // END_OF_WHILE
					{
						JsonObject jo = new JsonObject();
						jo.addProperty("type", "openai.response");
						jo.addProperty("data", sb_response.toString());
						pw.write("data: " + jo.toString() + "\n\n");
						pw.flush();
					}
					{
						logger.info("回答: " + sb_response.toString());
						history.addBotMessage("bot_" + session.getId(), q);
						logger.info("processing ... done");
					}

				} // END_OF_TRY (RESPONSE_FROM_OPEN_AI)
			} // END_OF_TRY(OPEN_AI)
			catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
				return;
			}
			logger.info("processing ... done");
		} // END_OF_TRY(Print)
	} // END_OF_doGet()

	private void removeVector(JsonObject responseObject) {
		{
			responseObject.remove("responseHeader");
			responseObject.addProperty("responseheader", "removed");
		}
		{
			JsonArray docs = responseObject.get("response").getAsJsonObject().get("docs").getAsJsonArray();
			for (int n = 0; n < docs.size(); n++) {
				docs.get(n).getAsJsonObject().remove("vector");
			}
		}
	}

}

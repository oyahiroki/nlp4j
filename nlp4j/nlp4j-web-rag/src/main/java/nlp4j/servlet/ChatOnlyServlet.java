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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import nlp4j.openai.Config;
import nlp4j.openai.OpenAI;

/**
 * Open AI API を単純に呼ぶ + セッション単位で会話履歴を保存する
 */
public class ChatOnlyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static private Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChatOnlyServlet() {
		super();
	}

	/**
	 *
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		ChatHistory history = (ChatHistory) session.getAttribute("chathistory");
		if (history == null) {
			history = new ChatHistory();
			session.setAttribute("chathistory", history);
		}

		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");

		final String openai_model = "gpt-4";

		logger.info("processing ...");

		// ユーザーからの質問
		String q = request.getParameter("q");

		history.addUserMessage("user_" + session.getId(), q);

		try (PrintWriter pw = response.getWriter();) {

			JsonObject objQuestion = new JsonObject();
			{
				objQuestion.addProperty("name", "question from end user");
				objQuestion.addProperty("value", q);
			}

			String content //
					= "{name:'question from end user'}は一般ユーザーからの質問です。必ず日本語で回答してください。\n" //
							+ "---\n" //
							+ objQuestion.toString() + "\n" // question
							+ "" //
							+ history.toString() //
							+ "" //
			;

			// Open AI API へのリクエスト
			JsonObject requestBody = new JsonObject();
			{
				requestBody.addProperty("model", openai_model);
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

			StringBuilder sb_response_all = new StringBuilder();

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
						// けっこう多い
						if (line.trim().isEmpty()) {
							continue;
						}
						if (line.length() < 10) {
							logger.warn("line.length<10:" + line);
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
//									System.out.print(s);
								sb_response.append(s);

								sb_response_all.append(s);
							}
						}

//							pw.write("data: " + line + "\n\n");

						JsonObject jo = new JsonObject();
						{
							jo.addProperty("type", "openai.stream");
							jo.add("data", (new Gson()).fromJson(line.substring(6), JsonObject.class));
						}

						pw.write("data: " + jo.toString() + "\n\n");
						pw.flush();

					} // END_OF_WHILE

					{
						JsonObject jo = new JsonObject();
						jo.addProperty("type", "openai.response");
						jo.addProperty("data", sb_response.toString());
						pw.write("data: " + jo.toString() + "\n\n");
						pw.flush();
					}

				} // END_OF_TRY (RESPONSE_FROM_OPEN_AI)

			} // END_OF_TRY(OPEN_AI)
			catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}

			logger.info("回答: " + sb_response_all);
			history.addBotMessage("bot_" + session.getId(), q);

		} // END_OF_TRY(Print)

		logger.info("processing ... done");

	} // END_OF_doGet()

} // END_OF_CLASS

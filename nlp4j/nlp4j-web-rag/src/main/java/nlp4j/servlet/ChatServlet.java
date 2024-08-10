package nlp4j.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import nlp4j.Document;
import nlp4j.openai.Config;
import nlp4j.openai.OpenAI;
import nlp4j.util.DateUtils;
import nlp4j.util.DocumentUtil;
import nlp4j.util.JsonObjectUtils;
import nlp4j.util.JsonUtils;
import nlp4j.util.RuntimeUtils;

/**
 * Servlet implementation class NlpServlet
 */
public class ChatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChatServlet() {
		super();
	}

	/**
	 *
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");

		String q = request.getParameter("q");

		try (PrintWriter pw = response.getWriter();) {

//			try {
//				Document doc = ServletUtils.parse(request);
//				response.setStatus(200);
//				pw.println(JsonUtils.prettyPrint(DocumentUtil.toJsonString(doc)));
//			} catch (IOException e) {
//				response.setStatus(400); // Bad Requiest
//				return;
//			}

			{
				String model = "gpt-4";
				String content = "What is the highest mountain in Japan?";
				JsonObject kb = new JsonObject();
				kb.addProperty("name_of_data", "Knowledge base for answering");
				JsonArray knowledgebase = new JsonArray();
				{
					{
						JsonObject knowledge = new JsonObject();
						knowledge.addProperty("knowledge", "CPSはCost Planning Sheetの略で、プロジェクトの予算管理に使われます");
						knowledgebase.add(knowledge);
					}
					{
						JsonObject knowledge = new JsonObject();
						knowledge.addProperty("knowledge", "CPSはIBMの社内用語です。");
						knowledgebase.add(knowledge);
					}
					{
						JsonObject knowledge = new JsonObject();
						knowledge.addProperty("knowledge_of_system_memory_status_not_cpu",
								RuntimeUtils.getMemoryInfo());
						knowledgebase.add(knowledge);
					}
					{
						JsonObject knowledge = new JsonObject();
						knowledge.addProperty("knowledge_of_system_timestamp", DateUtils.toISO8601(new Date()));
						knowledgebase.add(knowledge);
					}
					{
						JsonObject knowledge = new JsonObject();
						knowledge.addProperty("knowledge_of_system_total_capacity_of_root_directory_disk_in_bytes",
								(new File("/")).getTotalSpace() + " bytes");
						knowledgebase.add(knowledge);
					}
					{
						JsonObject knowledge = new JsonObject();
						knowledge.addProperty("knowledge_of_system_usable_space_of_root_directory_disk_in_bytes",
								(new File("/")).getUsableSpace() + " bytes");
						knowledgebase.add(knowledge);
					}
					{
						JsonObject knowledge = new JsonObject();
						knowledge.addProperty("knowledge_of_system_free_space_of_root_directory_disk_in_bytes",
								(new File("/")).getFreeSpace() + " bytes");
						knowledgebase.add(knowledge);
					}
					{
						JsonObject knowledge = new JsonObject();
						knowledge.addProperty("request_header_from_end_user_browser_accept_language",
								request.getHeader("Accept-Language"));
						knowledgebase.add(knowledge);
					}
					{
						JsonObject knowledge = new JsonObject();
						knowledge.addProperty("request_header_from_end_user_browser_user_agent",
								request.getHeader("User-Agent"));
						knowledgebase.add(knowledge);
					}

				}
				kb.add("knowledgebase", knowledgebase);
//				content = "日本で最も高い山の名前を教えてください。"
//						+ "必ず日本語で回答してください。";
				content = "{name_of_data:'question from end user'}は一般ユーザーからの質問です。必ず日本語で回答してください。"
						+ "{name_of_data:'Knowledge base for answering'}は回答のための知識ベースです。" + "極力知識ベースを用いて回答してください。\n" //
						+ "---\n" //
						+ "{name_of_data:'question from end user', value='" + q + "'}\n" // question
						+ "" + kb.toString() // knowledge base
				;

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

				System.out.println(JsonUtils.asProperties(requestBody));

				try (OpenAI openai = new OpenAI(new Config());) {

					try (BufferedReader br = new BufferedReader(
							new InputStreamReader(openai.chat_completions_stream(requestBody)))) {
						String line;
						while ((line = br.readLine()) != null) {
							// Send each chunk of data as an SSE (Server-Sent Event)
//							System.out.println("LINE: " + line + "");
//							System.out.println("LINE.length: " + line.length() + "");

							if (line.length() < 10) {
								continue;
							}

							if ("data: [DONE]".equals(line)) {
								break;
							}

							JsonObject r = (new Gson()).fromJson(line.substring("data: ".length()), JsonObject.class);
							{
								JsonElement je_content = r.get("choices").getAsJsonArray().get(0).getAsJsonObject()
										.get("delta").getAsJsonObject().get("content");
								if (je_content == null) {
									continue;
								}
								String s = je_content.getAsString();
								System.out.print(s);
							}

							pw.write("data: " + line + "\n\n");
							pw.flush();

						}
					}

					System.out.println("");

				} catch (Exception e) {

				}
			}

//			response.setContentType("application/javascript; charset=utf-8");

		}

	}

}

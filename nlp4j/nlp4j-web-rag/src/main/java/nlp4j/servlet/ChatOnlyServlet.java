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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import nlp4j.openai.Config;
import nlp4j.openai.OpenAI;

/**
 * Servlet implementation class NlpServlet
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

		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");

		String q = request.getParameter("q");

		try (PrintWriter pw = response.getWriter();) {

			{
				String model = "gpt-4";

				JsonObject objQuestion = new JsonObject();
				{
					objQuestion.addProperty("name_of_data", "question from end user");
					objQuestion.addProperty("value", q);
				}

				String content //
						= "{name_of_data:'question from end user'}は一般ユーザーからの質問です。必ず日本語で回答してください。\n" //
								+ "---\n" //
								+ objQuestion.toString() //
								+ "\n" // question
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

							if (logger.isDebugEnabled()) {
								JsonObject r = (new Gson()).fromJson(line.substring("data: ".length()),
										JsonObject.class);
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
									System.out.print(s);
								}
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

package nlp4j.openai;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class OpenAI_chat_completions_stream_Main1 {

	public static void main(String[] args) throws Exception {

		String model = "gpt-4";
		String content = "What is the highest mountain in Japan?";
		content = "日本で最も高い山の名前を教えてください。必ず日本語で回答してください。";
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
//					System.out.println("LINE: " + line + "");
//					System.out.println("LINE.length: " + line.length() + "");

					if (line.length() < 10) {
						continue;
					}

					if ("data: [DONE]".equals(line)) {
						break;
					}

					JsonObject r = (new Gson()).fromJson(line.substring("data: ".length()), JsonObject.class);
					{
						JsonElement je_content = r.get("choices").getAsJsonArray().get(0).getAsJsonObject().get("delta")
								.getAsJsonObject().get("content");
						if (je_content == null) {
							continue;
						}
						String s = je_content.getAsString();
						System.out.print(s);
					}

				}
			}

			System.out.println("");

		}
	}

}

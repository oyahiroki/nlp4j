package x;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import nlp4j.openai.Config;
import nlp4j.openai.OpenAI;
import nlp4j.util.JsonObjectUtils;
import nlp4j.util.JsonUtils;

public class MainChatGPT3_Response {

	public static void main(String[] args) throws Exception {

		String apiKey = System.getProperty("openai.api_key");
		String organization = System.getProperty("openai.organization");

		if (apiKey == null || organization == null) {
			System.err.println("null");
			return;
		}

//		File inputFile = new File("./file/Chat_MainChatGPT2.txt");
		File inputFile = new File("./file/Chat_MainChatGPT3.txt");

		String text = FileUtils.readFileToString(inputFile, "UTF-8");

		Config configuration = new Config(organization, apiKey);

		System.err.println(configuration);

		try (OpenAI openai = new OpenAI(configuration);) {

			{
//			JsonObject models = openai.models();
//			System.out.println(JsonUtils.prettyPrint(models));
			}

			{
//			String model = "gpt-3.5-turbo";
				String model = "gpt-4";
				String system_content = "You are a helpful assistant.";
//			String user_content = "こんにちは！";
				String user_content = text;

				JsonObject jo = new JsonObject();
				jo.addProperty("model", model);
				{
					JsonArray messages = new JsonArray();
					{
						JsonObject message = new JsonObject();
						message.addProperty("role", "system");
						message.addProperty("content", system_content);
						messages.add(message);
					}
					{
						JsonObject message = new JsonObject();
						message.addProperty("role", "user");
						message.addProperty("content", user_content);
						messages.add(message);
					}
					jo.add("messages", messages);
				}

//				String json = "{" //
//						+ "\"model\": \"" + model + "\"," //
//						+ "\"messages\": [" //
//						+ "{\"role\": \"system\", " //
//						+ "\"content\": \"" + system_content + "\"}, " //
//						+ "{\"role\": \"user\", \"content\": \"" + user_content + "\"}]," + "\"stream\":false" //
//						+ "}";
//				JsonObject requestbody = JsonObjectUtils.fromJson(json);
				JsonObject requestbody = JsonObjectUtils.fromJson(jo.toString());
				JsonObject response = openai.chat_completions(requestbody);
				System.out.println(JsonUtils.prettyPrint(response));
			}

		}
	}
}

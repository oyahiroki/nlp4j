package nlp4j.openai;

import java.util.Date;

import com.google.gson.JsonObject;

import nlp4j.util.JsonObjectUtils;
import nlp4j.util.JsonUtils;

public class MainChatGPT {

	public static void main(String[] args) throws Exception {

		String apiKey = System.getProperty("openai.api_key");
		String organization = System.getProperty("openai.organization");

		Configuration configuration = new Configuration(organization, apiKey);

		System.err.println(configuration);

		OpenAI openai = new OpenAI(configuration);

		{
//			JsonObject models = openai.models();
//			System.out.println(JsonUtils.prettyPrint(models));
		}

		{
//			String model = "gpt-3.5-turbo";
			String model = "gpt-4";
			String system_content = "You are a helpful assistant.";
//			String user_content = "こんにちは！";
			String user_content = "以下を１文に要約してください。要約した文のみを返してください。\n" //
					+ "\n" //
					+ "今日は晴れです。\n" //
					+ "今日は日曜日です。\n" //
					+ "明日は雨が降るでしょう\n" //
					+ "今日は2023年6月28日です。" //
					+ "";

			String json = "{" //
					+ "\"model\": \"" + model + "\"," //
					+ "\"messages\": [" //
					+ "{\"role\": \"system\", " //
					+ "\"content\": \"" + system_content + "\"}, " //
					+ "{\"role\": \"user\", \"content\": \"" + user_content + "\"}]," + "\"stream\":false" //
					+ "}";
			JsonObject requestbody = JsonObjectUtils.fromJson(json);
			JsonObject response = openai.chat_completions(requestbody);
			System.out.println(JsonUtils.prettyPrint(response));
		}

		System.out.println("---");

		{
			String model = "gpt-4";
			Messages messages = (new MessagesBuilder()) //
					.addSystem("You are a helpful assistant.") //
					.addUser("Who won the world series in 2020?") //
					.addAssistant("The Los Angeles Dodgers won the World Series in 2020.") //
					.addUser("Where was it played?") //
					.build();
			JsonObject response = openai.chat_completions(model, messages);
			System.out.println(JsonUtils.prettyPrint(response));
			{
				long created = response.get("created").getAsLong();
				Date date = new Date(created * 1000);
				System.out.println(date);
			}
		}

	}

}

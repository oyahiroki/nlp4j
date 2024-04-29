package nlp4j.openai;

public class MessagesBuilder {

	Messages messages = new Messages();

	public MessagesBuilder addSystem(String content) {
		this.messages.add("system", content);
		return this;
	}

	public MessagesBuilder addUser(String content) {
		this.messages.add("user", content);
		return this;
	}

	public MessagesBuilder addAssistant(String content) {
		this.messages.add("assistant", content);
		return this;
	}

	public Messages build() {
		return this.messages;
	}

}

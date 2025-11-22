package protocol.message;

public final class Message {

	private final String content;

	public Message(String content) {
		this.content = content;
	}

	public Message parseMessage(String contentString) {
		return new Message(contentString);
	}

	public MessageType getType() {
		return MessageType.BASIC;
	}

	public String getContent() {
		return content;
	}

	public String toString() {
		return this.content;
	}

}

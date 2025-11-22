package protocol.message;

public final class BasicMessage implements Message {

	private final MessageType type;
	private final String content;

	public BasicMessage(String content) {
		this.type = MessageType.BASIC;
		this.content = content;
	}

	public BasicMessage parseMessage(String contentString) {
		return new BasicMessage(contentString);
	}

	public MessageType getType() {
		return this.type;
	}

	public String getContent() {
		return content;
	}

	public String generateMessageString() {
		return this.getType() + " " + this.getContent();
	}

}

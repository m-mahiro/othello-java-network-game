package protocol.message;

public interface Message {

	String getMessageString();

	static MessageType getTypeFrom(String messageString) {
		String[] args = messageString.split(" ");
		try {
			return MessageType.valueOf(args[0]);
		} catch (IllegalArgumentException e) {
			throw MessageException.noSuchMessageType(messageString);
		}
	}

	MessageType getType();
}

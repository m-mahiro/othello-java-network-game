package controller.message;

public interface Message {

	String format();

	MessageType getType();

	static MessageType getTypeFrom(String messageString) {
		String[] args = messageString.split(" ");
		try {
			return MessageType.valueOf(args[0]);
		} catch (IllegalArgumentException e) {
			throw MessageException.noSuchMessageType(messageString);
		}
	}

}

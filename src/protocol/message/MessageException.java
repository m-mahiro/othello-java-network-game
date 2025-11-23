package protocol.message;

public class MessageException extends RuntimeException {

	public MessageException(String message) {
		super(message);
	}

	public static MessageException noSuchMessageType(String type) {
		return new MessageException("No such message type: " + type);
	};

	public static MessageException illegalMessageType(MessageType type) {
		return new MessageException("Illegal message type: " + type.toString());
	}

}

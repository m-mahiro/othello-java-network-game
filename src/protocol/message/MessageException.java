package protocol.message;

import protocol.packet.PacketException;
import protocol.packet.PacketType;

public class MessageException extends RuntimeException {

	public MessageException(String message) {
		super(message);
	}

	public static MessageException noSuchMessageType(String messageString) {
		return new MessageException("No such message type: " + messageString); // todo: 英語がおかしい
	}

	public static MessageException invalidMessageFormat(String messageString) {
		return new MessageException("Invalid message format: " + messageString);
	}

	public static MessageException illegalMessageType(MessageType type) {
		return new MessageException("Illegal message type: " + type.toString());
	}

	public static PacketException unsupportedMessageType(MessageType type) {
		return new PacketException("Unsupported message type: " + type);
	}

}

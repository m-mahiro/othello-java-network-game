package protocol.message;

public interface Message {

	String getMessageString();

	MessageType getType();

	static MessageType getTypeFrom(String messageString) {
		String[] args = messageString.split(" ");
		try {
			return MessageType.valueOf(args[0]);
		} catch (IllegalArgumentException e) {
			throw MessageException.noSuchMessageType(messageString);
		}
	}

	static Message parse(String bodyString){
		Message message;
		MessageType messageType = Message.getTypeFrom(bodyString);
		switch (messageType) {
			case BASIC:
				message = BasicMessage.parse(bodyString);
				break;
			case CLIENT_PROFILE:
				message = ClientConfigMessage.parse(bodyString);
			default:
				throw MessageException.noSuchMessageType(bodyString); // todo: 違うエラー内容の方が良いかな?
		}
		return message;
	}
}

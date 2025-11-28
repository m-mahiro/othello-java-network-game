package controller.message;

public class ClientProfileMessage implements Message {

	public static final MessageType type = MessageType.CLIENT_PROFILE;
	public final String clientName;

	private static final int headerSize = 1;

	public ClientProfileMessage(String clientName) {
		this.clientName = clientName;
	}

	public static ClientProfileMessage parse(String messageString) {

		MessageType type;
		String name;

		String[] args = messageString.split(" ");

		// メッセージタイプのエラーハンドリング
		try {
			type = Message.getTypeFrom(messageString);
			if (type != ClientProfileMessage.type) throw MessageException.illegalMessageType(type);
		} catch (IllegalArgumentException e) {
			throw MessageException.noSuchMessageType(args[0]);
		}

		char[] charArray = messageString.toCharArray();
		int count = 0;
		int bodyIndex = -1;

		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] == ' ') {
				count++;
			}
			if (count == headerSize) {
				bodyIndex = i;
				break;
			}
		}
		if (count != headerSize) throw MessageException.invalidMessageFormat(messageString);
		name = messageString.substring(bodyIndex + 1);

		return new ClientProfileMessage(name);
	}

	@Override
	public String format() {
		String str = "";
		str += ClientProfileMessage.type.toString() + " ";
		str += clientName;
		return str;
	}

	@Override
	public MessageType getType() {
		return ClientProfileMessage.type;
	}

	@Override
	public String toString() {
		return format();
	}

}
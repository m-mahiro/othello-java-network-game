package protocol.message;

import protocol.ParsingUtil;

public class ClientProfileMessage implements Message {

	public static final MessageType type = MessageType.CLIENT_PROFILE;
	public final String clientName;

	private static final int headerSize = 1;

	public ClientProfileMessage(String clientName) {
		this.clientName = clientName;
	}

	public static ClientProfileMessage parse(String messageString) {
		String[] args = messageString.split(" ");
		MessageType type;
		String name;

		// メッセージタイプのエラーハンドリング
		try {
			type = Message.getTypeFrom(messageString);
			if (type != ClientProfileMessage.type) throw MessageException.illegalMessageType(type);
		} catch (IllegalArgumentException e) {
			throw MessageException.noSuchMessageType(args[0]);
		}

		name = ParsingUtil.extractBody(messageString, headerSize);

		return new ClientProfileMessage(name);
	}

	@Override
	public String getMessageString() {
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
		return getMessageString();
	}

}
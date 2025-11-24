package protocol.message;

import protocol.ParsingUtil;
import protocol.packet.PacketException;

public final class BasicMessage implements Message {

	public static final MessageType type = MessageType.BASIC;
	public final String content;

	private static final int headerSize = 1;

	public BasicMessage(String content) {
		this.content = content;
	}

	public static BasicMessage parse(String messageString) {
		String[] args = messageString.split(" ");
		MessageType type;
		String content;

		// メッセージタイプのエラーハンドリング
		try {
			type = MessageType.valueOf(args[0]);
			if (type != BasicMessage.type) throw MessageException.illegalMessageType(type);
		} catch (IllegalArgumentException e) {
			throw MessageException.noSuchMessageType(args[0]);
		}

		content = ParsingUtil.extractBody(messageString, headerSize);

		return new BasicMessage(content);
	}

	@Override
	public String getMessageString() {
		String str = "";
		str += BasicMessage.type.toString() + " ";
		str += this.content;
		return str;
	}

	@Override
	public MessageType getType() {
		return BasicMessage.type;
	}

	@Override
	public String toString() {
		return getMessageString();
	}
}

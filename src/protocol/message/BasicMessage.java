package protocol.message;

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
		if (count != headerSize) {
			throw PacketException.invalidHeaderFormat(messageString) ;
		}
		content = messageString.substring(bodyIndex + 1);

		return new BasicMessage(content);
	}

	@Override
	public MessageType getType() {
		return BasicMessage.type;
	}

	@Override
	public String getMessageString() {
		return BasicMessage.type.toString() + " " + this.content;
	}

	@Override
	public String toString() {
		return getMessageString();
	}
}

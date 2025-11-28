package protocol.message;

public final class BasicMessage implements Message {

	public static final MessageType type = MessageType.BASIC;
	public final String content;

	private static final int headerSize = 1;

	public BasicMessage(String content) {
		this.content = content;
	}

	public static BasicMessage parse(String messageString) {
		MessageType type;
		String content;

		String[] args = messageString.split(" ");

		// メッセージタイプのエラーハンドリング
		try {
			type = Message.getTypeFrom(messageString);
			if (type != BasicMessage.type) throw MessageException.illegalMessageType(type);
		} catch (IllegalArgumentException e) {
			throw MessageException.noSuchMessageType(args[0]);
		}

		// bodyの抽出
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
		content = messageString.substring(bodyIndex + 1);

		return new BasicMessage(content);
	}

	@Override
	public String format() {
		String str = "";
		str += BasicMessage.type.toString() + " ";
		str += this.content;
		return str;
	}

	@Override
	public MessageType getType() {
		return BasicMessage.type;
	}

	public String toString() {
		return format();
	}
}

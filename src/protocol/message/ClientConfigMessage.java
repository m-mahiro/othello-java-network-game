package protocol.message;

public class ClientConfigMessage implements Message {

	public static final MessageType type = MessageType.CLIENT_CONFIG;
	public final int clientAddress;

	private static final int headerSize = 1;

	public ClientConfigMessage(int clientAddress) {
		this.clientAddress = clientAddress;
	}

	public static ClientConfigMessage parse(String messageString) {
		String[] args = messageString.split(" ");
		MessageType type;
		int address;

		// メッセージタイプのエラーハンドリング
		try {
			type = MessageType.valueOf(args[0]);
			if (type != ClientConfigMessage.type) throw MessageException.illegalMessageType(type);
		} catch (IllegalArgumentException e) {
			throw MessageException.noSuchMessageType(args[0]);
		}

		// bodyの抽出
		try {
			address = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			throw MessageException.invalidMessageFormat(messageString);
		}

		return new ClientConfigMessage(address);
	}

	@Override
	public String getMessageString() {
		String str = "";
		str += ClientConfigMessage.type.toString() + " ";
		str += this.clientAddress;
		return str;
	}

	@Override
	public MessageType getType() {
		return ClientConfigMessage.type;
	}

	@Override
	public String toString() {
		return getMessageString();
	}
}
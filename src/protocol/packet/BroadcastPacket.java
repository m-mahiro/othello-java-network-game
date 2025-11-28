package protocol.packet;

public class BroadcastPacket implements Packet {

	public final int source;
	public final String body;

	private static final PacketType type = PacketType.BROADCAST;
	private static final int headerSize = 2;

	public BroadcastPacket(int source, String body) {
		this.source = source;
		this.body = body;
	}

	public BroadcastPacket(String packetString) {
		// パケットタイプのエラーハンドリング
		PacketType type = Packet.getTypeFrom(packetString);
		if (type != BroadcastPacket.type) {
			throw PacketException.illegalPacketType(type);
		}

		// ヘッダーの各要素を取得する
		String[] args = packetString.split(" ");
		if (args.length < headerSize) {
			throw PacketException.invalidPacketFormat(packetString);
		}
		try {
			this.source = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			throw PacketException.invalidPacketFormat(packetString);
		}

		// bodyを取得する
		char[] charArray = packetString.toCharArray();
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
		if (count != headerSize) throw PacketException.invalidPacketFormat(packetString);
		this.body = packetString.substring(bodyIndex + 1);
	}

	public PacketType getType() {
		return BroadcastPacket.type;
	}

	@Override
	public String format() {
		String str = "";
		str += BroadcastPacket.type + " ";
		str += this.source + " ";
		str += this.body;
		return str;
	}

	@Override
	public String getBody() {
		return body;
	}

	@Override
	public boolean compareAddress(int address) {
		return true;
	}

	@Override
	public String toString() {
		return format();
	}

}
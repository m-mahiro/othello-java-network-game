package protocol.packet;

public class UnicastPacket implements Packet {

	public final int source;
	public final int destination;
	public final String body;

	private static final PacketType type = PacketType.UNICAST;
	private static final int headerSize = 3;

	public UnicastPacket(int source, int destination, String body) {
		this.source = source;
		this.destination = destination;
		this.body = body;
	}

	public UnicastPacket(String packetString) {
		// パケットタイプのエラーハンドリング
		PacketType type = Packet.getTypeFrom(packetString);
		if (type != UnicastPacket.type) throw PacketException.illegalPacketType(type);

		// ヘッダーの各要素を取得する
		String[] args = packetString.split(" ");
		if (args.length < headerSize) throw PacketException.invalidPacketFormat(packetString);
		try {
			this.source = Integer.parseInt(args[1]);
			this.destination = Integer.parseInt(args[2]);
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

	@Override
	public PacketType getType() {
		return UnicastPacket.type;
	}

	@Override
	public String getBody() {
		return body;
	}

	@Override
	public boolean compareAddress(int address) {
		return this.destination == address;
	}

	@Override
	public String format() {
		String str = "";
		str += UnicastPacket.type.toString() + " ";
		str += this.source + " ";
		str += this.destination + " ";
		str += body;
		return str;
	}

}
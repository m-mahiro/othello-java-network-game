package protocol.packet;

import protocol.message.*;

public class UnicastPacket implements Packet {

	public final int source;
	public final int destination;
	public final Message body;

	private static final PacketType type = PacketType.UNICAST;
	private static final int headerSize = 3;

	public UnicastPacket(int source, int destination, Message body) {
		this.source = source;
		this.destination = destination;
		this.body = body;
	}

	public static UnicastPacket parse(String packetString) {
		int source;
		int destination;
		Message body;

		// パケットタイプのエラーハンドリング
		PacketType type = Packet.getTypeFrom(packetString);
		if (type != UnicastPacket.type) throw PacketException.illegalPacketType(type);

		// ヘッダーの各要素を取得する
		String[] args = packetString.split(" ");
		if (args.length < headerSize) throw PacketException.invalidPacketFormat(packetString);
		try {
			source = Integer.parseInt(args[1]);
			destination = Integer.parseInt(args[2]);
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
		String bodyString = packetString.substring(bodyIndex + 1);
		body = Message.parse(bodyString);

		return new UnicastPacket(source, destination, body);
	}

	@Override
	public PacketType getType() {
		return UnicastPacket.type;
	}

	@Override
	public Message getBody() {
		return body;
	}

	@Override
	public String getPacketString() {
		String str = "";
		str += UnicastPacket.type.toString() + " ";
		str += this.source + " ";
		str += this.destination + " ";
		str += body.getMessageString();
		return str;
	}

	@Override
	public String toString() {
		return getPacketString();
	}

}
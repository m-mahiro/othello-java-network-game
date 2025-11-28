package protocol.packet;

import protocol.message.*;

public class BroadcastPacket implements Packet {

	public final int source;
	public final Message body;

	private static final PacketType type = PacketType.BROADCAST;
	private static final int headerSize = 2;

	public BroadcastPacket(int source, Message body) {
		this.source = source;
		this.body = body;
	}

	public static BroadcastPacket parse(String packetString) {

		int source;
		Message body;


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
			source = Integer.parseInt(args[1]);
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

		return new BroadcastPacket(source, body);
	}

	public PacketType getType() {
		return BroadcastPacket.type;
	}

	@Override
	public String getPacketString() {
		String str = "";
		str += BroadcastPacket.type + " ";
		str += this.source + " ";
		str += this.body.getMessageString();
		return str;
	}

	@Override
	public Message getBody() {
		return body;
	}

	@Override
	public String toString() {
		return getPacketString();
	}

}
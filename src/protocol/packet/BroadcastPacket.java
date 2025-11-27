package protocol.packet;

import protocol.ParsingUtil;
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

		// パケットタイプのエラーハンドリング
		PacketType packetType = Packet.getTypeFrom(packetString);
		if (packetType != BroadcastPacket.type) {
			throw PacketException.illegalPacketType(packetType);
		}

		// ヘッダーの各要素を取得する
		String[] args = packetString.split(" ");
		if (args.length < headerSize) {
			throw PacketException.invalidPacketFormat(packetString);
		}
		int source;
		try {
			source = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			throw PacketException.invalidPacketFormat(packetString);
		}

		// bodyを取得する
		String bodyString = ParsingUtil.extractBody(packetString, headerSize);
		Message message = Message.parse(bodyString);

		return new BroadcastPacket(source, message);
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
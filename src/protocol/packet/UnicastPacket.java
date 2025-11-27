package protocol.packet;

import protocol.ParsingUtil;
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

		// パケットタイプのエラーハンドリング
		PacketType packetType = Packet.getTypeFrom(packetString);
		if(packetType != UnicastPacket.type) {
			throw PacketException.illegalPacketType(packetType);
		}

		// ヘッダーの各要素を取得する
		String[] args =  packetString.split(" ");
		if (args.length < headerSize) {
			throw PacketException.invalidPacketFormat(packetString);
		}
		int source;
		int destination;
		try {
			source = Integer.parseInt(args[1]);
			destination = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			throw PacketException.invalidPacketFormat(packetString);
		}

		// bodyを取得する
		String bodyString = ParsingUtil.extractBody(packetString, headerSize);
		Message message = Message.parse(bodyString);

		return new UnicastPacket(source, destination, message);
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
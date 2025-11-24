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

		if (Packet.getTypeFrom(packetString) != BroadcastPacket.type) {
			throw PacketException.invalidHeaderFormat(packetString);
		}

		// ヘッダーの各要素を取得する
		String[] args = packetString.split(" ");
		if (args.length <= 1) {
			throw PacketException.invalidHeaderFormat(packetString);
		}
		int source;
		try {
			source = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			throw PacketException.invalidHeaderFormat(packetString);
		}

		// bodyの文字列を取得する todo: この処理は各パケットクラスで重複しています。
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
		if (count != headerSize) {
			throw PacketException.invalidHeaderFormat(packetString) ;
		}
		String bodyString = packetString.substring(bodyIndex + 1);

		// bodyStringをMessageオブジェクト化する
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
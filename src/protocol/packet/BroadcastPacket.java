package protocol.packet;

import protocol.message.*;

public class BroadcastPacket implements Packet {

	public final int source;
	public final Message body;

	private static final PacketType type = PacketType.BROADCAST;
	private static final int headerArgs = 2;

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
		int source = Integer.parseInt(args[1]);

		// bodyの文字列を取得する
		char[] charArray = packetString.toCharArray();
		int count = 0;
		int bodyIndex = -1;
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] == ' ') {
				count++;
			}
			if (count == headerArgs) {
				bodyIndex = i;
				break;
			}
		}
		if (count != headerArgs) {
			throw PacketException.invalidHeaderFormat(packetString) ;
		}
		String bodyString = packetString.substring(bodyIndex + 1);

		return new BroadcastPacket(source, new BasicMessage(bodyString));
		// todo: BasicMessage以外のメソッドが生まれたら、↑をなんとかする
	}

	public PacketType getType() {
		return BroadcastPacket.type;
	}

	@Override
	public String getPacketString() {
		String str = "";
		str += BroadcastPacket.type + " ";
		str += this.source;
		str += this.body.getMessageString();
		return str;
	}

	@Override
	public String toString() {
		return getPacketString();
	}

}
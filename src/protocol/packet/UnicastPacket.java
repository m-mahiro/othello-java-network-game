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

		if(!packetString.startsWith(UnicastPacket.type.toString() + " ")) {
			throw PacketException.invalidHeaderFormat(packetString);
		}

		// ヘッダーの各要素を取得する
		String[] args =  packetString.split(" ");
		int source = Integer.parseInt(args[1]);
		int destination = Integer.parseInt(args[2]);

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
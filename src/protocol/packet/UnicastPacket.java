package protocol.packet;

import protocol.message.*;

public final class UnicastPacket implements Packet{

	public static final int headerArgs = 3;
	public static final PacketType type = PacketType.UNICAST;
	private final int source;
	private final int destination;
	private final Message body;

	public UnicastPacket(int source, int destination, Message body) {
		this.source = source;
		this.destination = destination;
		this.body = body;
	}

	public static UnicastPacket parseUnicastPacket(String packetString) {

		if(packetString.startsWith(UnicastPacket.type.toString())) {
			throw PacketException.invalidHeaderFormat(packetString) ;
		}

		// ヘッダーの各要素を取得する
		String[] args =  packetString.split(" ");
		int source = Integer.parseInt(args[1]);
		int destination = Integer.parseInt(args[2]);

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

		return new UnicastPacket(source, destination, new BasicMessage(bodyString));
	}

	public String generatePacketString() {
		String str = "";
		str += UnicastPacket.type.toString() + " ";
		str += this.source + " ";
		str += this.destination + " ";
		str += body.toString();
		return str;
	}

	// ============== ゲッターメソッド ==============
	public int getSource() {
		return this.source;
	}

	public int getDestination() {
		return this.destination;
	}

	public Message getBody() {
		return this.body;
	}

}
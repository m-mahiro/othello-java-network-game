package protocol.packet;

public class Packet {

	public final int source;
	public final int destination;
	public final String body;

	public static final int SERVER_ADDRESS = 0;
	public static final int BROADCAST_ADDRESS = -1;

	private static final int HEADER_SIZE = 3;

	public Packet(int source, int destination, String body) {
		this.source = source;
		this.destination = destination;
		this.body = body;
	}

	public Packet(String packetString) {

		// ヘッダーの各要素を取得する
		String[] args = packetString.split(" ");
		if (args.length < HEADER_SIZE) throw PacketException.invalidPacketFormat(packetString);
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
			if (charArray[i] == ' ') count++;
			if (count == HEADER_SIZE) {
				bodyIndex = i;
				break;
			}
		}
		if (count != HEADER_SIZE) throw PacketException.invalidPacketFormat(packetString);
		this.body = packetString.substring(bodyIndex + 1);
	}

	public String getBody() {
		return body;
	}

	public String format() {
		String str = "";
		str += this.source + " ";
		str += this.destination + " ";
		str += body;
		return str;
	}

}
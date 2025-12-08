package network;

public class Packet {

	// パブック
	// HACK: SERVER_ADDRESSだけパッケージ外に公開する。
	public static final int SERVER_ADDRESS = 0;

	// パッケージプライベート
	final int source;
	final int destination;
	final String body;
	static final int BROADCAST_ADDRESS = -1;

	// プライベート
	private static final int HEADER_SIZE = 2;


	// ============================= パッケージプライベート =============================
	// HACK: メソッドは全てパッケージプライベート。SERVER_ADDRESSだけ、苦渋の決断でnetworkパッケージ外に公開することに。
	//  CommandIOの開発が関連して、そうしないと都合が悪い。
	Packet(int source, int destination, String body) {
		this.source = source;
		this.destination = destination;
		this.body = body;
	}

	Packet(String packetString) {

		// ヘッダーの各要素を取得する
		String[] args = packetString.split(" ");
		if (args.length < HEADER_SIZE) throw PacketException.invalidPacketFormat(packetString);
		try {
			this.source = Integer.parseInt(args[0]);
			this.destination = Integer.parseInt(args[1]);
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

	String format() {
		String str = "";
		str += this.source + " ";
		str += this.destination + " ";
		str += body;
		return str;
	}

	// ================== ゲッター / セッター ==================
	String getBody() {
		return body;
	}


}
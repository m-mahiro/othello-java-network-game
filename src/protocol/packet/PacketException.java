package protocol.packet;

public class PacketException extends RuntimeException {

	public PacketException(String errorMessage) {
		super(errorMessage);
	}

	public static PacketException noSuchPacketType(String packetString) {
		return new PacketException("No such packet type: " + packetString); // todo: 英語がおかしい
	}

	public static PacketException invalidHeaderFormat(String PacketString) {
		return new PacketException("Invalid header format: " + PacketString);
	}

	public static PacketException unsupportedPacketType(PacketType type) {
		return new PacketException("Unsupported packet type: " + type);
	}

}

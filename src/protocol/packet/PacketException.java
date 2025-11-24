package protocol.packet;

public class PacketException extends RuntimeException {

	public PacketException(String errorMessage) {
		super(errorMessage);
	}

	public static PacketException noSuchPacketType(String packetString) {
		return new PacketException("No such packet type: " + packetString); // todo: 英語がおかしい
	}

	public static PacketException invalidPacketFormat(String packetString) {
		return new PacketException("Invalid header format: " + packetString);
	}

	public static PacketException unsupportedPacketType(String packetString) {
		return new PacketException("Unsupported packet type: " + packetString);
	}

}

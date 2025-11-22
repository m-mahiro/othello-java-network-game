package protocol.packet;

public class PacketException extends RuntimeException {

	PacketException(String errorMessage) {
		super(errorMessage);
	}

	public static PacketException invalidHeaderFormat(String PacketString) {
		return new PacketException("Invalid header format: " + PacketString);
	}

}

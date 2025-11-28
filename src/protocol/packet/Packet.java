package protocol.packet;

public interface Packet {

	String format();

	PacketType getType();

	String getBody();

	boolean compareAddress(int address);

	static PacketType getTypeFrom(String packetString) {
		String[] args = packetString.split(" ");
		try {
			return PacketType.valueOf(args[0]);
		} catch (IllegalArgumentException e) {
			throw PacketException.noSuchPacketType(packetString);
		}
	}
}

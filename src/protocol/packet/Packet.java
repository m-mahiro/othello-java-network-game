package protocol.packet;

import protocol.message.Message;

public interface Packet {

	String getPacketString();


	PacketType getType();

	Message getBody();

	static PacketType getTypeFrom(String packetString) {
		String[] args = packetString.split(" ");
		try {
			return PacketType.valueOf(args[0]);
		} catch (IllegalArgumentException e) {
			throw PacketException.noSuchPacketType(packetString);
		}
	}

	static boolean compareAddress(Packet packet, int address) {
		switch (packet.getType()) {
			case BROADCAST:
				return false;
			case UNICAST:
				assert packet instanceof UnicastPacket;
				UnicastPacket unicastPacket = (UnicastPacket) packet;
				return unicastPacket.destination == address;
		}
		throw PacketException.unsupportedPacketType(packet.toString());
	}

}

package protocol.packet;

import server.network.ClientProcessThread;

public interface Packet {

	String getPacketString();

	static PacketType getTypeFrom(String packetString) {
		String[] args = packetString.split(" ");
		try {
			return PacketType.valueOf(args[0]);
		} catch (IllegalArgumentException e) {
			throw PacketException.noSuchPacketType(packetString);
		}
	}

	PacketType getType();

	static boolean compareAddress(Packet packet, ClientProcessThread me) {
		switch (packet.getType()) {
			case BROADCAST:
				return false;
			case UNICAST:
				assert packet instanceof UnicastPacket;
				UnicastPacket unicastPacket = (UnicastPacket) packet;
				return unicastPacket.destination == me.id;
		}
		throw new RuntimeException("想定外のパケットタイプ");
	}

}

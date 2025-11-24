package server.network;

import protocol.message.*;
import protocol.packet.*;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class ClientProcessThread extends Thread {

	public final int id;
	public String name;
	private final BufferedReader in;
	private final PrintWriter out;


	ClientProcessThread(int id, BufferedReader in, PrintWriter out, String clientName) {
		this.id = id;
		this.in = in;
		this.out = out;
		this.name = clientName;
	}

	public void run() {
		try {
			BasicMessage helloMessage = new BasicMessage("Hello, client No." + this.id + "!");
			MessageServer.send(helloMessage, this.id);

			while (true) {
				Message message;

				// メッセージを待つ
				String packetString = in.readLine(); // todo: IOExceptionをキャッチする?
				if (packetString == null) break; // ストリームラインの最後
				switch (Packet.getTypeFrom(packetString)) {
					case UNICAST:
						UnicastPacket unicastPacket = UnicastPacket.parse(packetString);
						if (Packet.compareAddress(unicastPacket, this)) {
							message = unicastPacket.body;
						} else {
							MessageServer.forward(unicastPacket);
							continue;
						}
						break;
					case BROADCAST:
						BroadcastPacket broadcastPacket = BroadcastPacket.parse(packetString);
						MessageServer.forward(broadcastPacket);
						continue;
					default:
						throw PacketException.noSuchPacketType(packetString);
				}

				System.out.println(message.getMessageString());
			}

		} catch (Exception e) {
			MessageServer.terminateClientProcess(this, e);
			throw new RuntimeException(e); // todo: ちゃんと定義したExceptionを使わないといけない
		}
	}

	public void send(Packet packet) {
		out.println(packet);
		out.flush();
		System.out.println(packet);
	}

}
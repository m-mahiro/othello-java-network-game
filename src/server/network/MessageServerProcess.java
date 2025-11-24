package server.network;

import protocol.message.*;
import protocol.packet.*;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class MessageServerProcess extends Thread {

	public final int address;
	public String name;
	private final BufferedReader in;
	private final PrintWriter out;


	MessageServerProcess(int address, BufferedReader in, PrintWriter out, String clientName) {
		this.address = address;
		this.in = in;
		this.out = out;
		this.name = clientName;
	}

	public void run() {
		try {
			BasicMessage helloMessage = new BasicMessage("Hello, client No." + this.address + "!");
			MessageServer.send(helloMessage, this.address);

			while (true) {
				Message message;

				// メッセージを待つ
				String packetString = in.readLine(); // todo: IOExceptionをキャッチする?
				if (packetString == null) break; // ストリームラインの最後
				switch (Packet.getTypeFrom(packetString)) {
					case UNICAST:
						UnicastPacket unicastPacket = UnicastPacket.parse(packetString);
						if (Packet.compareAddress(unicastPacket, this.address)) {
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
						throw PacketException.noSuchPacketType(packetString); // todo: 違うエラー内容の方が良いかな?
				}

				System.out.println("[MessageServerProcess] " + message.getMessageString());
			}

		} catch (Exception e) {
//			MessageServer.terminateClientProcess(this, e);
			throw new RuntimeException(e); // todo: ちゃんと定義したExceptionを使わないといけない
		}
	}

	public void push(Packet packet) {
		out.println(packet);
		out.flush();
		System.out.println("[MessageServerProcess] " + packet);
	}

}
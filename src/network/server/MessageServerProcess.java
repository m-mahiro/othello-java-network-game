package network.server;

import protocol.message.*;
import protocol.packet.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class MessageServerProcess extends Thread {

	private int address;
	private String clientName;
	private final BufferedReader in;
	private final PrintWriter out;

	MessageServerProcess(int address, BufferedReader in, PrintWriter out) {

		this.address = address;
		this.in = in;
		this.out = out;
		this.start();
	}

	public void run() {
		try {
			// クライアントにアドレスを通知する。
			out.println(address);

			// メッセージの受け取りを開始する
			Message message;
			while (true) {
				// メッセージを待つ
				String packetString = in.readLine();
				if (packetString == null) break; // ストリームラインの最後
				PacketType packetType = Packet.getTypeFrom(packetString);
				switch (packetType) {
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
						throw PacketException.unsupportedPacketType(packetType);
				}
				System.out.println("[MessageServerProcess] " + message.getMessageString());
			}

		} catch (IOException e) {
//			MessageServer.terminateClientProcess(this, e);
			throw new RuntimeException(e); // todo: ちゃんと定義したExceptionを使わないといけない
		}
	}

	public void push(Packet packet) {
		out.println(packet);
		out.flush();
		System.out.println("[MessageServerProcess] " + packet);
	}

	// ================== ゲッター / セッター ==================
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public int getAddress() {
		return this.address;
	}
}
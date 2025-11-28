package network.server;

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

		// クライアントにアドレスを通知する。
		out.println(address);

		this.start();
	}

	private void log(String method, String string) {
		if (method.equals("()")) {
			System.out.println("[BroadcastPacket()] " + string);
		} else {
			System.out.println("[BroadcastPacket." + method + "()] " + string);
		}

	}

	public void run() {
		try {
			// メッセージの受け取りを開始する
			String message;
			while (true) {
				// メッセージを待つ
				String packetString = in.readLine();
				if (packetString == null) break; // ストリームラインの最後

				// Packetインスタンス化
				PacketType packetType = Packet.getTypeFrom(packetString);
				switch (packetType) {
					case UNICAST:
						UnicastPacket unicastPacket = new UnicastPacket(packetString);
						if (unicastPacket.compareAddress(this.address)) {
							message = unicastPacket.body;
						} else {
							MessageServer.forward(unicastPacket);
							continue;
						}
						break;
					case BROADCAST:
						BroadcastPacket broadcastPacket = new BroadcastPacket(packetString);
						MessageServer.forward(broadcastPacket);
						continue;
					default:
						throw PacketException.unsupportedPacketType(packetType);
				}
				log("run" ,message);
			}

		} catch (IOException e) {
//			MessageServer.terminateClientProcess(this, e);
			throw new RuntimeException(e); // todo: ちゃんと定義したExceptionを使わないといけない
		}
	}

	public void push(Packet packet) {
		out.println(packet);
		out.flush();
		log("push" ,packet.toString());
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
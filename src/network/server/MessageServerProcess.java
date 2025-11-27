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
			// クライアントにアドレスを通知する
			ClientConfigMessage clientConfigMessage =  new ClientConfigMessage(address);
			MessageServer.send(clientConfigMessage, this.address);

			// クライアントからプロフィールを受け取る
			try {
				Message message = this.waitMessage();
				// todo: ProfileMessageは受け取っているけど、waitMessageがはじいちゃう。
				if (message.getType() == MessageType.CLIENT_PROFILE) {
					ClientProfileMessage clientProfileMessage = (ClientProfileMessage) message;
					this.setClientName(clientProfileMessage.clientName);
					System.out.println("[MessageServerProcess] Noticed client name! (clientName: " + clientProfileMessage.clientName + ")");
				}
			} catch (IOException e) {
				throw new RuntimeException("クライアントからプロフィール受け取れませんでした。"); // todo: ちゃんとしたExceptionを定義する
			}

			while (true) {
				Message message = waitMessage();
				System.out.println("[MessageServerProcess] " + message.getMessageString());
			}

		} catch (Exception e) {
//			MessageServer.terminateClientProcess(this, e);
			throw new RuntimeException(e); // todo: ちゃんと定義したExceptionを使わないといけない
		}
	}

	private Message waitMessage() throws IOException {

		Message message = null;

		while(true) {
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
		}
		return message;
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
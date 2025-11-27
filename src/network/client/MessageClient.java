package network.client;

import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;
import protocol.message.*;
import protocol.packet.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MessageClient extends Thread {

	private BufferedReader in;
	private PrintWriter out;
	private int address;
	private String clientName;

	private final int SERVER_ADDRESS = 0;

	public MessageClient(String clientName) {

		this.clientName = clientName;

		// 通信路の確立
		try {
			@SuppressWarnings("resource")
			Socket socket = new Socket("localhost", 10000);
			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			this.in = new BufferedReader(inputStreamReader);
			this.out = new PrintWriter(socket.getOutputStream());

		} catch (UnknownHostException e) {
			System.out.println("[MessageClient] " + "ホストのIPアドレスが判定できません。: " + e);
		} catch (IOException e) {
			System.out.println("[MessageClient] " + "エラーが発生しました。" + e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public void run() {
		Message message;
		try {
			// サーバからの通知を基に、クライアントの設定をする
			message = this.waitMessage();
			if (message.getType() == MessageType.CLIENT_CONFIG) {
				ClientConfigMessage clientConfigMessage = (ClientConfigMessage) message;
				this.setAddress(clientConfigMessage.clientAddress);
				System.out.println("[MessageClient] Address Config Done! (address: " + address + ")");
			}

			// クライアントのプロフィールをサーバに通知する
			ClientProfileMessage clientProfileMessage = new ClientProfileMessage(clientName);
			UnicastPacket packet = new UnicastPacket(this.address, SERVER_ADDRESS, clientProfileMessage);
			this.transport(packet); // 接続して初めの一行はclientName todo: 気に入らない

			while (true) {
				message = this.waitMessage();
				if (message == null) break;
				System.out.println("[MessageClient] " + message);
			}

		} catch (IOException e) {
			System.out.println("[MessageClient] " + e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void transport(Packet packet) {
		out.println(packet);
		out.flush();
		System.out.println("[MessageClient] " + packet);
	}

	public void broadcast(Message message) {
		BroadcastPacket packet = new BroadcastPacket(this.address, message);
		transport(packet);
	}

	public void send(int destination, Message message) {
		UnicastPacket packet = new UnicastPacket(this.address, destination, message);
		transport(packet);
	}


	private Message waitMessage() throws IOException {
		String packetString = in.readLine();
		if (packetString == null) return null;

		// Packetオブジェクト化
		Packet packet;
		PacketType packetType = Packet.getTypeFrom(packetString);
		switch (packetType) {
			case UNICAST:
				packet = UnicastPacket.parse(packetString);
				break;
			case BROADCAST:
				packet = BroadcastPacket.parse(packetString);
				break;
			default:
				throw PacketException.unsupportedPacketType(packetType); // todo: 違うエラー内容の方が良いかな?
		}
		return packet.getBody();
	}

	// ================== ゲッター / セッター ==================
	public void setAddress(int address) {
		this.address = address;
	}

	public int getAddress() {
		return this.address;
	}
}
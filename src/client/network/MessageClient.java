package client.network;

import protocol.message.BasicMessage;
import protocol.message.Message;
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
	public final int id = 1;

	public MessageClient(String clientName) {
		try {
			// 通信路の確立
			Socket socket = new Socket("localhost", 10000);
			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			this.in = new BufferedReader(inputStreamReader);
			this.out = new PrintWriter(socket.getOutputStream());

			// clientNameをサーバに送信する
			BasicMessage message = new BasicMessage(clientName);
			UnicastPacket packet = new UnicastPacket(this.id, 0, message);
			this.transport(packet); // 接続して初めの一行はclientName todo: 気に入らない

			// clientIdの通知を受ける
			Message helloMessage = this.waitMessage(); // "Hello, client No.1!" を受け取る
			System.out.println("[MessageClientThread] " + helloMessage);

		} catch (UnknownHostException e) {
			System.out.println("[MessageClientThread] " + "ホストのIPアドレスが判定できません。: " + e);
		} catch (IOException e) {
			System.out.println("[MessageClientThread] " + "エラーが発生しました。" + e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public void run () {
		while(true) {
			try {
				Message message = this.waitMessage();
				if (message == null) break;
				System.out.println("[MessageClientThread] " + message);
			} catch (IOException e) {
				System.out.println("[MessageClientThread] " + e.getMessage());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void transport(Packet packet) {
		out.println(packet);
		out.flush();
		System.out.println("[MessageClientThread] " + packet);
	}

	public void broadcast(Message message) {
		BroadcastPacket packet = new BroadcastPacket(this.id, message);
		transport(packet);
	}

	public void send(int destination, Message message) {
		UnicastPacket packet = new UnicastPacket(this.id, destination, message);
		transport(packet);
	}


	public Message waitMessage() throws Exception {
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
				throw PacketException.noSuchPacketType(packetString); // todo: 違うエラー内容の方が良いかな?
		}

		return packet.getBody();
	}

}
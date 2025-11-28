package network.client;

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

	private final int SERVER_ADDRESS = 0;
	
	private void log(String method, String string) {
		if (method.equals("()")) {
			System.out.println("[MessageClient()] " + string);
		} else {
			System.out.println("[MessageClient." + method + "()] " + string);
		}
	}

	public MessageClient() {
		try {
			// 通信路の確立
			@SuppressWarnings("resource")
			Socket socket = new Socket("localhost", 10000);
			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			this.in = new BufferedReader(inputStreamReader);
			this.out = new PrintWriter(socket.getOutputStream());

			// サーバからアドレスの通知を受ける
			this.address = Integer.parseInt(in.readLine());
			log("()", "Address Config Done! (address: " + this.address + ")");

			// メッセージの受け取りを開始
			this.start();

		} catch (UnknownHostException e) {
			log("()","ホストのIPアドレスが判定できません。: " + e);
		} catch (IOException e) {
			log("()" ,"エラーが発生しました。" + e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void run() {

		try {
			// この処理を関数化してはいけない。run()以外でメッセージを受け取ることを想定していないから。
			String body;
			while (true) {
				String packetString = in.readLine();
				if (packetString == null) break;

				// Packetオブジェクト化
				Packet packet;
				PacketType packetType = Packet.getTypeFrom(packetString);
				switch (packetType) {
					case UNICAST:
						packet = new UnicastPacket(packetString);
						break;
					case BROADCAST:
						packet = new BroadcastPacket(packetString);
						break;
					default:
						throw PacketException.unsupportedPacketType(packetType);
				}

				// bodyを取得
				body = packet.getBody();
				log("run" ,body);
			}

		} catch (IOException e) {
			log("run" ,e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void transport(Packet packet) {
		out.println(packet);
		out.flush();
		log("transport" , packet.toString());
	}

	public void broadcast(String message) {
		BroadcastPacket packet = new BroadcastPacket(this.address, message);
		transport(packet);
	}

	public void send(int destination, String message) {
		UnicastPacket packet = new UnicastPacket(this.address, destination, message);
		transport(packet);
	}

	// ================== ゲッター / セッター ==================
	public void setAddress(int address) {
		this.address = address;
	}

	public int getAddress() {
		return this.address;
	}
}
package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MessageClient {

	private BufferedReader in;
	private PrintWriter out;
	private int address;
	private String host;
	private int port;

	public MessageClient(String host, int port) {
		this.host = host;
		this.port = port;
		try {
			// 通信路の確立
			@SuppressWarnings("resource")
			Socket socket = new Socket(host, port);
			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			this.in = new BufferedReader(inputStreamReader);
			this.out = new PrintWriter(socket.getOutputStream());

			// サーバからアドレスの通知を受ける
			this.address = Integer.parseInt(in.readLine());
			log("()", "Address Config Done! (address: " + this.address + ")");

		} catch (UnknownHostException e) {
			log("()", "ホストのIPアドレスが判定できません。: " + e);
		} catch (IOException e) {
			log("()", "エラーが発生しました。" + e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void broadcast(String message) {
		Packet packet = new Packet(this.address, Packet.BROADCAST_ADDRESS, message);
		transport(packet);
	}

	public void send(String message, int destination) {
		Packet packet = new Packet(this.address, destination, message);
		transport(packet);
	}

	public void sendToServer(String message) {
		Packet packet = new Packet(this.address, Packet.SERVER_ADDRESS, message);
		transport(packet);
	}

	public String nextMessage() {
		// クライアント一つにつき、1つのスレッドである必要がある
		try {
			// 文字列を受信する
			String packetString = in.readLine();
			log("nextMessage", "packetString: " + packetString);
			if (packetString == null) {
				throw new RuntimeException("ファイルストリームの最後に達しました。");
			}

			// Packetオブジェクト化
			if (packetString.contains("\n")) throw new RuntimeException("受信内容に改行文字が含まれていました。");
			Packet packet = new Packet(packetString);

			// bodyを取得
			String body = packet.getBody();
			log("nextMessage", "Fetched: " + body);
			return body;

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	// ================== ゲッター / セッター ==================
	public void setAddress(int address) {
		this.address = address;
	}

	public int getAddress() {
		return this.address;
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}


	// ================== プライベートメソッド ==================
	private void transport(Packet packet) {
		String message = packet.format();
		if (message.contains("\n")) throw new RuntimeException("送信内容に改行文字を入れてはいけません。");
		out.println(message);
		out.flush();
		log("transport", packet.format());
	}

	private void log(String method, String string) {
		if (method.equals("()")) {
			System.out.println("[MessageClient()] " + string);
		} else {
			System.out.println("[MessageClient." + method + "()] " + string);
		}
	}

}
package network;

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
	private int threadCount = 0;
	private String host;
	private int port;

	private void log(String method, String string) {
		if (method.equals("()")) {
			System.out.println("[MessageClient()] " + string);
		} else {
			System.out.println("[MessageClient." + method + "()] " + string);
		}
	}

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

	@Override
	public void run() {
		// クライアント一つにつき、1つのスレッドである必要がある
		threadCount++;
		if (threadCount > 1) throw new RuntimeException("2つ以上のスレッドは開始できません。");

		try {
			// この処理を関数化してはいけない。run()以外でメッセージを受け取ることを想定していないから。
			String body;
			while (true) {

				// 文字列を受信する
				String packetString = in.readLine();
				if (packetString == null) {
					log("run", "ファイルストリームの最後に達しました。スレッドを終了します。");
					break; // todo: ここはExceptionを吐くべき?
				}

				// Packetオブジェクト化
				Packet packet = new Packet(packetString);

				// bodyを取得
				body = packet.getBody();
				log("run" ,"Fetched: " + body);
			}

		} catch (IOException e) {
			log("run" ,e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void broadcast(String message) {
		Packet packet = new Packet(this.address, Packet.BROADCAST_ADDRESS, message);
		transport(packet);
	}

	public void send(int destination, String message) {
		Packet packet = new Packet(this.address, destination, message);
		transport(packet);
	}


	// ================== ゲッター / セッター ==================
	public void setAddress(int address) {
		this.address = address;
	}

	public int getAddress() {
		return this.address;
	}

	String getHost() {
		return this.host;
	}

	int getPort() {
		return this.port;
	}


	// ================== プライベートメソッド ==================
	private void transport(Packet packet) {
		out.println(packet.format());
		out.flush();
		log("transport" , packet.format());
	}
}
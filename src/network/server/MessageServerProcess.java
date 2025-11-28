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

	private void log(String method, String string) {
		if (method.equals("()")) {
			System.out.println("[BroadcastPacket()] " + string);
		} else {
			System.out.println("[BroadcastPacket." + method + "()] " + string);
		}

	}

	MessageServerProcess(int address, BufferedReader in, PrintWriter out) {

		this.address = address;
		this.in = in;
		this.out = out;

		// クライアントにアドレスを通知する。
		out.println(address);

		this.start();
	}

	public void run() {
		try {
			String message;

			// メッセージの受け取りを開始する
			while (true) {

				// パケットを待つ
				String packetString = in.readLine();
				if (packetString == null) break; // ストリームラインの最後
				Packet packet = new Packet(packetString);

				// 受け取たパケットは全てMessageServerに任せる
				MessageServer.forward(packet);
				log("run", "Fetched: " + packet);
			}

		} catch (IOException e) {
			MessageServer.terminateClientProcess(this, e);
			throw new RuntimeException(e); // todo: ちゃんと定義したExceptionを使わないといけない
		}
	}

	public void push(Packet packet) {
		out.println(packet);
		out.flush();
		log("push", packet.toString());
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
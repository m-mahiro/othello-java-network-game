package network.server;

import network.Packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MessageServer {

	private final int MAX_CONNECTION;
	private final int PORT;
	private final HashMap<Integer, MessageServerProcess> clients = new HashMap<>();

	private void log(String method, String string) {
		if (method.equals("()")) {
			System.out.println("[BroadcastPacket()] " + string);
		} else {
			System.out.println("[BroadcastPacket." + method + "()] " + string);
		}
	}

	public MessageServer(int port, int maxConnection) {
		this.PORT = port;
		this.MAX_CONNECTION = maxConnection;
		log("()" ,"The Server has launched!");

		// クライアントの受付を開始
		int address = 0;
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(PORT);
			while (true) {

				// 通信路を確立
				Socket socket = server.accept();
				InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
				BufferedReader in = new BufferedReader(inputStreamReader);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

				// サーバープロセスを生成
				address++;
				MessageServerProcess client = new MessageServerProcess(address, in, out);
				this.registerClient(client);
				client.start();
				log("main" ,"Accept client No." + address);
				if (address > MAX_CONNECTION) throw new RuntimeException("接続数をおーばしました。"); // todo: ちゃんとしたエクセプションに
			}

		} catch (Exception e) {
			log("main" ,"サーバの待ちソケット作成時にエラーが発生しました: " + e);
		}
	}

	public void send(String message, int destination) {
		Packet packet = new Packet(Packet.SERVER_ADDRESS, destination, message); // 0はサーバのアドレス
		this.forward(packet);
	}


	// ================== パッケージプライベート ==================
	void forward(Packet packet) {
		switch (packet.destination) {
			// ブロードキャスト
			case Packet.BROADCAST_ADDRESS: {
				for (MessageServerProcess client : clients.values()) {
					client.push(packet);
				}
				// ここにbreakがないのはわざと
			}

			// サーバ宛て
			case Packet.SERVER_ADDRESS: {
				// ブロードキャストの場合も、サーバは受信する。
				System.out.println("Receive: " + packet.format());
				break;
			}

			// ユニキャスト
			default: {
				MessageServerProcess client = clients.get(packet.destination);
				client.push(packet);
				break;
			}
		}
	}


	// ================== プライベートメソッド ==================
	private void terminateClientProcess(MessageServerProcess client, Exception e) {
		log("terminateClientProcess","Disconnect from client No."+client.getAddress());
		clients.remove(client.getAddress());
	}

	private void registerClient(MessageServerProcess client) {
		MessageServer.this.clients.put(client.getAddress(), client);
	}


	// ========================================== インナークラス ================================================
	// インナークラスである必要がある理由:
	// このクラスはMessageServerの忠実なしもべなので、ほかの人のいいなりになってはいけないから。
	// このクラスはMessageClientとMessageServerの架け橋というだけなので、他からアクセスできてはならないから。
	// =======================================================================================================
	class MessageServerProcess extends Thread {

		private int address;
		private final BufferedReader in;
		private final PrintWriter out;

		private void log(String method, String string) {
			if (method.equals("()")) {
				System.out.println("[MessageServerProcess()] " + string);
			} else {
				System.out.println("[MessageServerProcess." + method + "()] " + string);
			}

		}

		// MessageServerからしかインスタンスを生成できない
		MessageServerProcess(int address, BufferedReader in, PrintWriter out) {
			this.address = address;
			this.in = in;
			this.out = out;
			out.println(address); // クライアントにアドレスを通知する。;
		}

		@Override
		public void run() {
			try {
				// メッセージの受け取りを開始する
				while (true) {

					// パケットを待つ
					String packetString = in.readLine();
					if (packetString == null) break; // todo: ここはExceptionを吐くべき?
					Packet packet = new Packet(packetString);

					// 受け取たパケットは全てMessageServerに任せる
					MessageServer.this.forward(packet);
					log("run", "Fetched: " + packet);
				}

			} catch (IOException e) {
				MessageServer.this.terminateClientProcess(this, e);
				throw new RuntimeException(e); // todo: ちゃんと定義したExceptionを使わないといけない
			}
		}

		void push(Packet packet) {
			out.println(packet);
			out.flush();
			log("push", packet.toString());
		}

		// ================== ゲッター / セッター ==================
		void setAddress(int address) {
			this.address = address;
		}

		int getAddress() {
			return this.address;
		}
	}

}
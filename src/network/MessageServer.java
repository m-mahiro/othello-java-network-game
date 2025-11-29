package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MessageServer extends Thread {

	private final int maxConnection;
	private final int port;
	private final HashMap<Integer, MessageServerProcess> clients = new HashMap<>();
	private int threadCount = 0;

	private void log(String method, String string) {
		if (method.equals("()")) {
			System.out.println("[BroadcastPacket()] " + string);
		} else {
			System.out.println("[BroadcastPacket." + method + "()] " + string);
		}
	}

	public MessageServer(int port, int maxConnection) {
		this.port = port;
		this.maxConnection = maxConnection;
		log("()" ,"The Server has launched!");
		this.start();
	}

	@Override
	public void run() {
		threadCount++;
		if (threadCount > 1) throw new RuntimeException("2つ以上のスレッドは開始できません。");

		// クライアントの受付を開始
		int address = Math.max(Packet.SERVER_ADDRESS, Packet.BROADCAST_ADDRESS);
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(port);
			while (true) {
				if (address > maxConnection) {
					continue;
				}

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
				log("run" ,"Accept (address: " + address + ")");
			}

		} catch (Exception e) {
			log("run" ,"サーバの待ちソケット作成時にエラーが発生しました: " + e);
		}
	}

	public void send(String message, int destination) {
		Packet packet = new Packet(Packet.SERVER_ADDRESS, destination, message); // 0はサーバのアドレス
		this.forward(packet);
	}

	public void broadcast(String message) {
		Packet packet = new Packet(Packet.SERVER_ADDRESS, Packet.BROADCAST_ADDRESS, message);
		this.forward(packet);
	}

	public int getConcurrentConnections() {
		return clients.size();
	}


	// ================== プライベートメソッド ==================
	private void terminateClientProcess(MessageServerProcess client, Exception e) {
		log("terminateClientProcess","Disconnect from client No."+client.getAddress());
		clients.remove(client.getAddress());
	}

	private void registerClient(MessageServerProcess client) {
		MessageServer.this.clients.put(client.getAddress(), client);
	}

	private void forward(Packet packet) {
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
				log("forward", "Receive: " + packet.format());
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


	// ========================================== インナークラス ================================================
	// インナークラスである必要がある理由:
	// このクラスはMessageServerの忠実なしもべなので、ほかの人のいいなりになってはいけないから。
	// このクラスはMessageClientとMessageServerの架け橋というだけなので、他からアクセスできてはならないから。
	// =======================================================================================================
	class MessageServerProcess extends Thread {

		private final int address;
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
					if (packetString == null) {
						log("run", "ファイルストリームの最後に達しました。スレッドを終了します。");
						break; // todo: ここはExceptionを吐くべき?
					}
					Packet packet = new Packet(packetString);
					log("run", "Fetched: " + packet.format());

					// 受け取たパケットは全てMessageServerに任せる
					MessageServer.this.forward(packet);
				}

			} catch (IOException e) {
				MessageServer.this.terminateClientProcess(this, e);
				throw new RuntimeException(e); // todo: ちゃんと定義したExceptionを使わないといけない
			}
		}

		void push(Packet packet) {
			out.println(packet.format());
			out.flush();
			log("push", packet.format());
		}

		// ================== ゲッター / セッター ==================
		int getAddress() {
			return this.address;
		}
	}
}
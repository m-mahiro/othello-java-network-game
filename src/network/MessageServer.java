package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageServer extends Thread {

	private final int maxConnection;
	private final int port;
	private final ConcurrentHashMap<Integer, ClientProcessThread> clients = new ConcurrentHashMap<>();
	private final BlockingQueue<Packet> messageQueue = new LinkedBlockingQueue<>();

	public MessageServer(int port, int maxConnection) {
		this.port = port;
		this.maxConnection = maxConnection;
		log("()" ,"The Server has launched!");
		this.start();
	}

	public String nextMessage() {
		try {
			Packet packet = this.messageQueue.take();
			return packet.getBody();
		} catch (InterruptedException e) {
			throw new RuntimeException("パケットをキューから取り出す際に割込みが発生しました。: " + e);
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


	@Override
	public void run() {
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
				ClientProcessThread client = new ClientProcessThread(address, in, out);
				this.registerClient(client);
				client.start();
				log("run" ,"Accept (address: " + address + ")");
			}

		} catch (Exception e) {
			log("run" ,"サーバの待ちソケット作成時にエラーが発生しました: " + e);
		}
	}


	// ================== プライベートメソッド ==================
	private void terminateClientProcess(ClientProcessThread client, Exception e) {
		log("terminateClientProcess","Disconnect from client No."+client.getAddress());
		clients.remove(client.getAddress());
	}

	// todo: これ関数化する必要ある?
	private void registerClient(ClientProcessThread client) {
		MessageServer.this.clients.put(client.getAddress(), client);
	}

	private void forward(Packet packet) {
		switch (packet.destination) {

			// ブロードキャスト or サーバ宛て
			case Packet.BROADCAST_ADDRESS:
				for (ClientProcessThread client : clients.values()) client.push(packet);
				// breakがないのはわざと
			case Packet.SERVER_ADDRESS:
				log("forward", "Receive: " + packet.format());
				try {
					MessageServer.this.messageQueue.put(packet);
				} catch (InterruptedException e) {
					throw new RuntimeException("パケットをキューに追加する際に割込みが発生しました。" + e);
				}
				break;

			// ユニキャスト
			default:
				ClientProcessThread client = clients.get(packet.destination);
				client.push(packet);
				break;
		}
	}


	// ========================================== インナークラス ================================================
	// インナークラスである必要がある理由:
	// このクラスはMessageServerの忠実なしもべなので、ほかの人のいいなりになってはいけないから。
	// このクラスはMessageClientとMessageServerの架け橋というだけなので、他からアクセスできてはならないから。
	// todo: この理由であれば、インナークラスではなく同じファイルのprivateクラスを検討した方が良いかも。その方が可読性高い?変わらない?
	// =======================================================================================================
	private class ClientProcessThread extends Thread {

		private final int address;
		private final BufferedReader in;
		private final PrintWriter out;

		// MessageServerからしかインスタンスを生成できない
		ClientProcessThread(int address, BufferedReader in, PrintWriter out) {
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
			log("push", "Pushed: " + packet.format());
		}

		// ================== ゲッター / セッター ==================
		int getAddress() {
			return this.address;
		}

		// ================== プライベートメソッド ==================
		private void log(String method, String string) {
			if (method.equals("()")) {
				System.out.println("[ClientProcessThread()] " + string);
			} else {
				System.out.println("[ClientProcessThread." + method + "()] " + string);
			}

		}
	}

	// ================== プライベートメソッド ==================
	private void log(String method, String string) {
		if (method.equals("()")) {
			System.out.println("[MessageServer()] " + string);
		} else {
			System.out.println("[MessageServer." + method + "()] " + string);
		}
	}

}
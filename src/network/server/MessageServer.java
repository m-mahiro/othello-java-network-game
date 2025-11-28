package network.server;


import network.Packet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MessageServer {

	private final static int MAX_CONNECTION = 100;
	private static final HashMap<Integer, MessageServerProcess> clients = new HashMap<>();

	private static void log(String method, String string) {
		if (method.equals("()")) {
			System.out.println("[BroadcastPacket()] " + string);
		} else {
			System.out.println("[BroadcastPacket." + method + "()] " + string);
		}

	}

	public static void main(String[] args) {
		int address = 0;
		log("main" ,"The Server has launched!");

		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(10000);
			while (true) {

				// 通信路を確立
				Socket socket = server.accept();
				InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
				BufferedReader in = new BufferedReader(inputStreamReader);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

				// サーバープロセスを生成
				address++;
				MessageServerProcess client = new MessageServerProcess(address, in, out);
				MessageServer.registerClient(client);
				log("main" ,"Accept client No." + address);

				if (address > MAX_CONNECTION) throw new RuntimeException("接続数をおーばしました。"); // todo: ちゃんとしたエクセプションに
			}

		} catch (Exception e) {
			log("main" ,"サーバの待ちソケット作成時にエラーが発生しました: " + e);

		}
	}

	public static void send(String message, int destination) {
		Packet packet = new Packet(Packet.SERVER_ADDRESS, destination, message); // 0はサーバのアドレス
		MessageServer.forward(packet);
	}

	public static void terminateClientProcess(MessageServerProcess client, Exception e) {
		log("terminateClientProcess","Disconnect from client No."+client.getAddress() +"("+client.getClientName()+")");
		clients.remove(client.getAddress());
	}

	public static void registerClient(MessageServerProcess client) {
		MessageServer.clients.put(client.getAddress(), client);
	}

	// ================== パッケージプライベート ==================
	static void forward(Packet packet) {
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

//	todo: アドレスが枯渇したときの話。

}
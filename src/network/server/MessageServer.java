package network.server;

import protocol.message.Message;
import protocol.packet.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MessageServer {
	final private static int maxConnection = 100;
	private static final HashMap<Integer, MessageServerProcess> clients = new HashMap<>();
	private static final int SERVER_ADDRESS = 0;
	
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
			}

		} catch (Exception e) {
			log("main" ,"サーバの待ちソケット作成時にエラーが発生しました: " + e);

		}
	}

	public static void forward(UnicastPacket packet) {
		if (packet.destination == SERVER_ADDRESS) {
			log("forward" ,packet.getPacketString());
			// todo: ここで、サーバー側のコントローラの何かを呼ぶ(依存関係の方向に注意）
			return;
		}
		MessageServerProcess destinationClient = clients.get(packet.destination);
		if (destinationClient == null) {
			log("forward", "Error: Destination client with ID " + packet.destination + " does not exist. Packet not delivered.");
			return;
		}
		destinationClient.push(packet);
	}

	public static void forward(BroadcastPacket packet) {
		log("forward" ,packet.getPacketString());
		// todo: ここで、サーバー側のコントローラの何かを呼ぶ(依存関係の方向に注意）
		for (MessageServerProcess client : clients.values()) {
			client.push(packet);
		}
	}

	public static void send(Message message, int destination) {
		UnicastPacket packet = new UnicastPacket(SERVER_ADDRESS, destination, message); // 0はサーバのアドレス
		MessageServer.forward(packet);
	}

	public static void terminateClientProcess(MessageServerProcess client, Exception e) {
		log("terminateClientProcess","Disconnect from client No."+client.getAddress() +"("+client.getClientName()+")");
		clients.remove(client.getAddress());
	}

	public static void registerClient(MessageServerProcess client) {
		MessageServer.clients.put(client.getAddress(), client);
	}

//	todo: アドレスが枯渇したときの話。

}
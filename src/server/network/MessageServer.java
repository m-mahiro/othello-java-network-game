package server.network;

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
	private static final int CLIENT_ID_FOR_SERVER = 0;

	public static void main(String[] args) {
		int clientId = 0;
		System.out.println("[MessageServer] " + "The Server has launched!");

		try (ServerSocket server = new ServerSocket(10000)) {

			while (true) {
				Socket socket = server.accept();
				System.out.println("[MessageServer] " + "Accept client No." + clientId);
				clientId++;
				InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
				BufferedReader in = new BufferedReader(inputStreamReader);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				String clientName = in.readLine(); // 接続して初めの一行はclientName todo: 気に入らない
				MessageServerProcess client = new MessageServerProcess(clientId, in, out, clientName);
				clients.put(clientId, client);
				client.start();
			}

		} catch (Exception e) {
			System.out.println("[MessageServer] " + "サーバの待ちソケット作成時にエラーが発生しました: " + e);

		}
	}

	public static void forward(UnicastPacket packet) {
		if (packet.destination == CLIENT_ID_FOR_SERVER) {
			System.out.println("[MessageServer] " + packet.getPacketString());
			// todo: ここで、サーバー側のコントローラの何かを呼ぶ(依存関係の方向に注意）
			return;
		}
		MessageServerProcess destinationClient = clients.get(packet.destination);
		destinationClient.push(packet);
	}

	public static void forward(BroadcastPacket packet) {
		System.out.println("[MessageServer] " + packet.getPacketString());
		// todo: ここで、サーバー側のコントローラの何かを呼ぶ(依存関係の方向に注意）
		for (MessageServerProcess client : clients.values()) {
			client.push(packet);
		}
	}

	public static void send(Message message, int destination) {
		UnicastPacket packet = new UnicastPacket(0, destination, message); // 0はサーバのクライアントID
		MessageServer.forward(packet);
	}

	public static void terminateClientProcess(MessageServerProcess client, Exception e) {
		System.out.println("[MessageServer] " + "Disconnect from client No."+client.id +"("+client.name+")");
		clients.remove(client.id);
	}

//	todo: clientIdが枯渇したときの話。

}
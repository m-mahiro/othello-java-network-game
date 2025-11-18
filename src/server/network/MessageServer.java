package server.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;

public class MessageServer {
	final private static int maxConnection = 100;
	private static HashMap<Integer, ClientProcessThread> clients = new HashMap<>();

	public static void main(String[] args) {
		int clientId = 0;
		System.out.println("The Server has launched!");

		try (ServerSocket server = new ServerSocket(10000)) {

			while (true) {
				Socket socket = server.accept();
				System.out.println("Accept client No." + clientId);
				clientId++;
				InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
				BufferedReader in = new BufferedReader(inputStreamReader);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				String clientName = in.readLine(); // 接続して初めの一行はclientName todo: 気に入らない
				ClientProcessThread client = new ClientProcessThread(clientId, in, out, clientName);
				clients.put(clientId, client);
				client.start();
			}

		} catch (Exception e) {
			System.out.println("サーバの待ちソケット作成時にエラーが発生しました: " + e);

		}
	}


	public static void sendAll(String str, ClientProcessThread sender) {
		System.out.println("Broadcast(" + sender.clientId + "->*) : " + str);
		for (ClientProcessThread client : clients.values()) {
			System.out.print("    ");
			client.send(str);
		}
	}



	public static void terminateClientProcess(ClientProcessThread client, Exception e) {
		System.out.println("Disconnect from client No."+client.clientId+"("+client.name+")");
		clients.remove(client.clientId);
	}

//	todo: clientIdが枯渇したときの話。

}
package server.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class OthelloServer {
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
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
				ClientProcessThread client = new ClientProcessThread(clientId, bufferedReader,printWriter);
				clients.put(clientId, client);
			}

		} catch (Exception e) {
			System.out.println("サーバの待ちソケット作成時にエラーが発生しました: " + e);

		}
	}

	public static void terminateClientProcess(ClientProcessThread client) {
		System.out.println("Disconnect from client No."+client.clientId+"("+client.name+")");
		clients.remove(client.clientId);
	}

}
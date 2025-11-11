package server.network;

import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

public class OthelloServer {
	final private static int maxConnection = 100;
	private static HashMap<Integer, ClientProcessThread> clients;

	public static void main(String args) {
		int clientId = 0;
		System.out.println("The Server has launched!");

		try {
			ServerSocket server = new ServerSocket(10000);
			while(true) {
				Socket socket = server.accept();
				System.out.println("Accept client No." + clientId);
				InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());

			}

		} catch (Exception e) {
			System.out.println("サーバの待ちソケット作成時にエラーが発生しました: " + e);

		}
	}

}
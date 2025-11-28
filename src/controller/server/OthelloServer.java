package controller.server;

import network.MessageServer;

import java.util.Scanner;

public class OthelloServer {
	public static void main(String[] args) {
		MessageServer server = new MessageServer(10000, 50);
		while (true) {
			Scanner sc = new Scanner(System.in);
			String message = sc.nextLine();
			if (message.equals("exit")) break;
			server.broadcast(message);
		}
	}
}
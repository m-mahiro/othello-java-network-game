package client.controller;


import client.network.MessageClientThread;
import server.network.ClientProcessThread;

import java.util.*;

public class MessageSendStub {

	public static void main(String[] args) {
		MessageClientThread messageClientThread = new MessageClientThread("test-user");

		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print("Enter message to send (type 'exit' to quit): ");
			String input = sc.nextLine();
			if (input.equalsIgnoreCase("exit")) {
				break;
			}
			messageClientThread.send(input);
		}
		sc.close();

	}


}
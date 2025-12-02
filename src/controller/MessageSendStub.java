package controller;

import network.MessageClient;
import java.util.*;

public class MessageSendStub {

	public static void main(String[] args) {
		MessageClient messageClient = new MessageClient("localhost", 10000);

		Scanner sc = new Scanner(System.in);
		while (true) {
			String message = sc.nextLine();
			if (message.equalsIgnoreCase("exit")) {
				break;
			}
			messageClient.broadcast(message);
		}
		sc.close();
	}
}
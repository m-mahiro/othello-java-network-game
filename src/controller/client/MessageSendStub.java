package controller.client;

import network.client.MessageClient;
import protocol.packet.*;
import java.util.*;

public class MessageSendStub {

	public static void main(String[] args) {
		MessageClient messageClient = new MessageClient();

		Scanner sc = new Scanner(System.in);
		while (true) {
			String message = sc.nextLine();
			if (message.equalsIgnoreCase("exit")) {
				break;
			}
			Packet packet = new BroadcastPacket(messageClient.getAddress(), message);
			messageClient.transport(packet);
		}
		sc.close();
	}


}
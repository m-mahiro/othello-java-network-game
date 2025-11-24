package client.controller;


import client.network.MessageClient;
import protocol.message.*;
import protocol.packet.*;

import java.util.*;

public class MessageSendStub {

	public static void main(String[] args) {
		MessageClient messageClient = new MessageClient("test-user");

		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print("BroadCast: ");
			String input = sc.nextLine();
			if (input.equalsIgnoreCase("exit")) {
				break;
			}
			Message message = new BasicMessage(input);
			Packet packet = new BroadcastPacket(messageClient.address, message);
			messageClient.transport(packet);
		}
		sc.close();
	}


}
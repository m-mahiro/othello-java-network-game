package controller;

import model.Othello;
import network.MessageClient;

public class OthelloClient extends Thread {

	private final MessageClient messageClient;
	private final Othello othello;

	public OthelloClient() {
		this.messageClient = new MessageClient("localhost", 10000);
		this.othello = new Othello();
	}
}

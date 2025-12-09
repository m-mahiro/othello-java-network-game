package controller;

import network.MessageClient;
import network.MessageServer;

class ClientCommander {

	private final ServerCommandIO serverCommandIO;
	private final int opponentAddress;

	ClientCommander(ServerCommandIO serverCommandIO, int opponentAddress) {
		this.serverCommandIO = serverCommandIO;
		this.opponentAddress = opponentAddress;
	}

	void playWith(int address, String name) {
		ClientCommand command = ClientCommand.playWith(address, name);
		this.issue(command);
	}

	void putCoin(int i, int j) {
		ClientCommand command = ClientCommand.putCoin(i, j);
		this.issue(command);
	}

	void revert() {
		ClientCommand command = ClientCommand.revert();
		this.issue(command);
	}

	void restart() {
		ClientCommand command = ClientCommand.restart();
		this.issue(command);
	}

	private void issue(ClientCommand command) {
		serverCommandIO.push(command, opponentAddress);
	}
}


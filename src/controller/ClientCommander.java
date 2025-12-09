package controller;

import network.MessageClient;

class ClientCommander {

	private final ClientCommandIO clientCommandIO;
	private final int opponentAddress;

	ClientCommander(MessageClient messageClient, int opponentAddress) {
		this.clientCommandIO = new ClientCommandIO(messageClient);
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
		clientCommandIO.push(command, opponentAddress);
	}
}


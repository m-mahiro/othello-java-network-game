package controller;

import network.MessageClient;

class ServerCommander {

	private final ClientCommandIO clientCommandIO;

	ServerCommander(MessageClient messageClient) {
		this.clientCommandIO = new ClientCommandIO(messageClient);
	}

	void registerClient(int address, String name) {
		ServerCommand command = ServerCommand.registerClient(address, name);
		this.issue(command);
	}

	void searchOpponent() {
		ServerCommand command = ServerCommand.searchOpponent();
		this.issue(command);
	}

	private void issue(ServerCommand command) {
		clientCommandIO.push(command);
	}
}

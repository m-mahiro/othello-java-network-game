package controller;

import network.MessageClient;

class ServerCommander {

	private final ClientCommandIO clientCommandIO;

	ServerCommander(ClientCommandIO clientCommandIO) {
		this.clientCommandIO = clientCommandIO;
	}


	// ============================= コマンドを発行するメソッド群 =============================
	void registerClient(String name) {
		int address = clientCommandIO.getAddress();
		ServerCommand command = ServerCommand.registerClient(address, name);
		this.issue(command);
	}

	void searchOpponent() {
		ServerCommand command = ServerCommand.searchOpponent();
		this.issue(command);
	}

	// ============================= プライベートメソッド =============================
	private void issue(ServerCommand command) {
		clientCommandIO.push(command);
	}
}

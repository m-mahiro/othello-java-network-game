package controller;

import model.Coin;
import network.MessageClient;
import network.MessageServer;

class ClientCommander {

	private final ServerCommandIO serverCommandIO;
	private final int opponentAddress;

	ClientCommander(ServerCommandIO serverCommandIO, int opponentAddress) {
		this.serverCommandIO = serverCommandIO;
		this.opponentAddress = opponentAddress;
	}


	// ============================= コマンドを発行するメソッド群 =============================
	void playWith(int address, String name, Coin coin) {
		ClientCommand command = ClientCommand.playWith(address, name, coin);
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

	// ============================= プライベートメソッド =============================
	private void issue(ClientCommand command) {
		serverCommandIO.push(command, opponentAddress);
	}
}


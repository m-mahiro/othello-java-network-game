package controller;

class ClientCommander {

	private final ClientCommandIO clientCommandIO;
	private final int opponentAddress;

	ClientCommander(int opponentAddress) {
		this.clientCommandIO = new ClientCommandIO();
		this.opponentAddress = opponentAddress;
	}

	void playWith(int address, String name) {
		ClientCommand command = ClientCommand.playWith(address, name);
		this.push(command);
	}

	void putCoin(int i, int j) {
		ClientCommand command = ClientCommand.putCoin(i, j);
		this.push(command);
	}

	void revert() {
		ClientCommand command = ClientCommand.revert();
		this.push(command);
	}

	void restart() {
		ClientCommand command = ClientCommand.restart();
		this.push(command);
	}

	private void push(ClientCommand command) {
		clientCommandIO.push(command, opponentAddress);
	}
}


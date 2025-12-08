package controller;

class ClientCommander {

	public void playWith(int address, String name) {
		ClientCommand.Type type = ClientCommand.Type.PLAY_WITH;
		String[] args = {Integer.toString(address), name};
		ClientCommand command = new ClientCommand(type, args);
	}

	public void putCoin(int i, int j) {
		ClientCommand.Type type = ClientCommand.Type.PUT_COIN;
		String[] args = {Integer.toString(i), Integer.toString(j)};
		ClientCommand command = new ClientCommand(type, args);
	}

	public void revert() {
		ClientCommand.Type type = ClientCommand.Type.REVERT;
		String[] arg = {};
		ClientCommand command = new ClientCommand(type, arg);
	}

	public void restart() {
		ClientCommand.Type type = ClientCommand.Type.RESTART;
		String[] arg = {};
		ClientCommand command = new ClientCommand(type, arg);
	}
}


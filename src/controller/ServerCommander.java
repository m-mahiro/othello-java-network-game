package controller;


class ServerCommander {

	public void registerClient(int address, String name) {
		ServerCommand.Type type = ServerCommand.Type.REGISTER_CLIENT;
		String[] args = {Integer.toString(address), name};
		ServerCommand command = new ServerCommand(type, args); // HACK: コマンドの引数に関する知識が、CommandクラスとCommanderとに散らばっている。
	}

	public void searchOpponent() {
		ServerCommand.Type type = ServerCommand.Type.SEARCH_OPPONENT;
		String[] args = {};
		ServerCommand command = new ServerCommand(type, args);
	}

}

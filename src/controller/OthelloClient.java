package controller;


import model.Othello;
import network.MessageClient;

public class OthelloClient {

	Othello othello;
	// OthelloView othelloView;

	public OthelloClient() {
		CommandClient commandClient = new CommandClient();

	}

	// ============================= インナークラス =============================
	// NOTE: なぜインナークラスなのか?
	//  例えば、ClientCommandにOthelloClientを渡すことになるけど、
	//  そのときにrun()とかそういうメッセージサーバ関連を触らせるわけにはいかない。
	private class CommandClient extends Thread {

		private MessageClient messageClient;

		public CommandClient() {
			this.messageClient = new MessageClient("localhsot", 10000);
			this.start();
		}

		@Override
		public void run() {
			String commandString = this.messageClient.nextMessage();
			ClientCommand command = new ClientCommand(OthelloClient.this, commandString);
			command.execute();
		}

		public void sendCommand(ClientCommand clientCommand) {

		}

		public void sendCommand(ServerCommand serverCommand) {

		}
	}
}


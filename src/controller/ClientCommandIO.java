package controller;

import network.MessageClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class ClientCommandIO extends Thread {

	private final MessageClient messageClient; // この部分だけ具象クラスで共通していない。

	// NOTE: 排他制御は必要ないけど、取り出すときに空なら追加されるまで待つという特性が便利だからつかう。
	private final BlockingQueue<ServerCommand> serverCommandQueue = new LinkedBlockingQueue<>();
	private final BlockingQueue<ClientCommand> clientCommandQueue = new LinkedBlockingQueue<>();

	ClientCommandIO() {
		// NOTE: MessageIOを作成するのは自分自身。コントローラのリーダーに任せない。
		this.messageClient = new MessageClient("localhost", 100);
	}

	void push(ClientCommand clientCommand, int clientAddress) {
		this.messageClient.send(clientCommand.format(), clientAddress);
	}

	void push(ServerCommand serverCommand) {
		this.messageClient.sendToServer(serverCommand.format());
	}

	ClientCommand nextClientCommand() {
		try {
			return this.clientCommandQueue.take();
		} catch (InterruptedException e) {
			throw new RuntimeException("クライアントコマンドをキューから取り出す際に割込みが発生しました。");
		}
	}

	ServerCommand nextServerCommand() {
		try {
			return this.serverCommandQueue.take();
		} catch (InterruptedException e) {
			throw new RuntimeException("サーバコマンドをキューから取り出す際に割込みが発生しました。");
		}

	}

	@Override
	public void run() {

		String message = messageClient.nextMessage();
		if (message.startsWith(Command.SERVER_COMMAND.toString())) {
			String commandString = message.substring(Command.SERVER_COMMAND.toString().length() + 1);
			ServerCommand command = new ServerCommand(commandString);
			this.serverCommandQueue.add(command);

		} else if (message.startsWith(Command.CLIENT_COMMAND.toString())) {
			String commandString = message.substring(Command.CLIENT_COMMAND.toString().length() + 1);
			ClientCommand command = new ClientCommand(commandString);
			this.clientCommandQueue.add(command);

		} else {
			throw new AssertionError();
		}
	}


}
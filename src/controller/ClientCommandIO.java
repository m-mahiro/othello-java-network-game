package controller;

import network.MessageClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class ClientCommandIO extends Thread {

	private final MessageClient messageClient; // この部分だけ具象クラスで共通していない。

	// NOTE: 排他制御は必要ないけど、取り出すときに空なら追加されるまで待つという特性が便利だからつかう。
	private final BlockingQueue<ClientCommand> clientCommandQueue = new LinkedBlockingQueue<>();

	ClientCommandIO() {
		this.messageClient = new MessageClient("localhost", 10000);
		this.start();
	}

	ClientCommand nextClientCommand() {

		try {
			ClientCommand command = this.clientCommandQueue.take();
			log("nextClientCommand", command.format());
			return command;
		} catch (InterruptedException e) {
			throw new RuntimeException("クライアントコマンドをキューから取り出す際に割込みが発生しました。");
		}
	}

	void push(ServerCommand serverCommand) {
		String message = CommandHeader.SERVER_COMMAND.toString() + serverCommand.format();
		this.messageClient.sendToServer(message);
	}

	@Override
	public void run() {

		while (true) {
			String message = messageClient.nextMessage();
			log("run", "message: " + message);
			if (! message.startsWith(CommandHeader.CLIENT_COMMAND.toString())) {
				throw CommandException.invalidMessageFormat(message);
			}
			String commandString = message.substring(CommandHeader.CLIENT_COMMAND.toString().length());
			log("run", "commandString: " + commandString);
			ClientCommand command = new ClientCommand(commandString);
			this.clientCommandQueue.add(command);
		}
	}



	// ============================= デバッグ用 =============================
	private void log(String method, String string) {
		if (method.equals("()")) {
			System.out.println("[ClientCommandIO()] " + string);
		} else {
			System.out.println("[ClientCommandIO" + method + "()] " + string);
		}
	}
}
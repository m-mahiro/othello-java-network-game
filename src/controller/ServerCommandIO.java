package controller;

import network.MessageServer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class ServerCommandIO extends Thread {

	private final MessageServer messageServer; // この部分だけ具象クラスで共通していない。

	// NOTE: 排他制御は必要ないけど、取り出すときに空なら追加されるまで待つという特性が便利だからつかう。
	private final BlockingQueue<ServerCommand> serverCommandQueue = new LinkedBlockingQueue<>();

	ServerCommandIO(MessageServer messageServer) {
		// NOTE: なぜ、ClientCommandIOではコンストラクタ内でMessageClientの起動も行うのに、こちらでは引数として受け取るのか？
		//  MessageServerが単純にクライアントとサーバをつなぐものなら、CommandIOがMessageServerを起動してもいいと思うけど、
		//  MessageServerは転送も行うものなので、ここで起動を行うのは越権行為
		this.messageServer = messageServer;
	}

	ServerCommand nextServerCommand() {
		try {
			return this.serverCommandQueue.take();
		} catch (InterruptedException e) {
			throw new RuntimeException("サーバコマンドをキューから取り出す際に割込みが発生しました。");
		}
	}

	void push(ClientCommand clientCommand, int clientAddress) {
		this.messageServer.send(clientCommand.format(), clientAddress);
	}


	@Override
	public void run() {

		String message = messageServer.nextMessage();
		if (message.startsWith(CommandHeader.SERVER_COMMAND.toString())) {
			String commandString = message.substring(CommandHeader.SERVER_COMMAND.toString().length() + 1);
			ServerCommand command = new ServerCommand(commandString);
			this.serverCommandQueue.add(command);

		} else {
			throw new AssertionError();
		}
	}


}


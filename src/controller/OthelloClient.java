package controller;

import model.Coin;
import model.Othello;
import network.MessageClient;

public class OthelloClient extends Thread {

	private final ClientCommandIO clientCommandIO;
	private final Othello othello;

	public OthelloClient() {
		MessageClient messageClient = new MessageClient("localhost", 10000);
		this.clientCommandIO = new ClientCommandIO(messageClient);
		this.othello = new Othello(Coin.BLACK);
		CommandReceiveThread thread = new CommandReceiveThread();
		thread.start();
	}

	@Override
	public void run() {
		// ここにオセロゲームのメインスレッドを記述
		// できればアクティビティごとにクラスを作成したいけど......絶対間に合わんくなるよな......

		// こちらは送信
		ClientCommander clientCommander = new ClientCommander(2);
		clientCommander.putCoin(1, 2);
	}


	// ============================= デバッグ用 =============================
	private void log(String method, String string) {
		if (method.equals("()")) {
			System.out.println("[OthelloClient()] " + string);
		} else {
			System.out.println("[OthelloClient" + method + "()] " + string);
		}
	}



	// ============================= インナークラス =============================
	// NOTE: なぜインナークラスなのか？
	//  OthelloClientでもスレッドを動かしたいから。
	// ========================================================================
	private class CommandReceiveThread extends Thread {
		@Override
		public void run() {
			ClientCommand command = clientCommandIO.nextClientCommand();
			log("run", command.format());
//			command.executeOn(OthelloClient.this);
		}

		// ============================= デバッグ用 =============================
		private void log(String method, String string) {
			if (method.equals("()")) {
				System.out.println("[OthelloClient.CommandReceiveThread()] " + string);
			} else {
				System.out.println("[OthelloClient.CommandReceiveThread" + method + "()] " + string);
			}
		}

	}

}

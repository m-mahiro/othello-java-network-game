package controller;

import model.Coin;
import model.Othello;
import network.MessageClient;

public class OthelloClient extends Thread {

	private final MessageClient messageClient; // SMELL: レイヤーとばしてる。折角CommandIOで隠ぺいしたのに。
	private final ClientCommandIO clientCommandIO;
	private final Othello othello;

	public OthelloClient(MessageClient messageClient) {

		// 初期化
		this.messageClient = messageClient;
		this.clientCommandIO = new ClientCommandIO(messageClient);
		this.othello = new Othello(Coin.BLACK);

		// メインロジックは別スレッドで
		CommandReceiveThread thread = new CommandReceiveThread();
		thread.start();
	}

	@Override
	public void run() {
		// ここにオセロゲームのメインスレッドを記述

		ServerCommander serverCommander = new ServerCommander(messageClient);



//		(new Scanner(System.in)).nextInt();
	}

	// ============================= ゲッター/セッター =============================
	Othello getOthello() {
		return this.othello;
	}

	// ============================= デバッグ用 =============================
	private void log(String method, String string) {
		if (method.equals("()")) {
			System.out.println("[OthelloClient()] " + string);
		} else {
			System.out.println("[OthelloClient" + method + "()] " + string);
		}
	}


	// ============================= プライベートインナークラス =============================
	// NOTE: なぜインナークラスなのか？
	//  OthelloClientでもスレッドを動かしたいから。
	// ========================================================================

	private class CommandReceiveThread extends Thread {
		@Override
		public void run() {
			ClientCommand command = clientCommandIO.nextClientCommand();
			log("run", command.format());
			command.executeOn(OthelloClient.this);
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

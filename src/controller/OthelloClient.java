package controller;

import model.Coin;
import model.Othello;

public class OthelloClient extends Thread {

	private final ClientCommandIO clientCommandIO;
	private final Othello othello;

	public OthelloClient() {
		this.clientCommandIO = new ClientCommandIO();
		this.othello = new Othello(Coin.BLACK);
		CommandReceiveThread thread = new CommandReceiveThread();
		thread.start();
	}

	@Override
	public void run() {
		// ここにオセロゲームのメインスレッドを記述
		// できればアクティビティごとにクラスを作成したいけど......絶対間に合わんくなるよな......

		ClientCommander clientCommander = new ClientCommander();

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

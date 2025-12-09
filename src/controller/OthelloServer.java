package controller;

import model.Coin;
import java.util.Scanner;

public class OthelloServer extends Thread {

	private final ServerCommandIO serverCommandIO;
	public OthelloServer() {
		this.serverCommandIO = new ServerCommandIO();
		CommandReceiveThread thread = new CommandReceiveThread();
		thread.start();
	}

	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		sc.nextInt();
		ClientCommander clientCommander = new ClientCommander(this.serverCommandIO, 1);

		clientCommander.playWith(2, "すばる", Coin.BLACK);
		clientCommander.putCoin(1, 1);
		clientCommander.restart();
		clientCommander.revert();


		while(true) {}
	}

	// ============================= インナークラス =============================
	// NOTE: なぜインナークラスなのか？
	//  OthelloServerでもスレッドを動かしたいから。
	// =======================================================================
	private class CommandReceiveThread extends Thread {
		@Override
		public void run() {
			while(true) {
				ServerCommand command = serverCommandIO.nextServerCommand();
				log("run", command.format());
				command.executeOn(OthelloServer.this);
			}
		}
	}

	// ============================= デバッグ用 =============================
	private void log(String method, String string) {
		if (method.equals("()")) {
			System.out.println("[OthelloServer()] " + string);
		} else {
			System.out.println("[OthelloServer." + method + "()] " + string);
		}
	}
}

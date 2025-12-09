package controller;

import network.MessageServer;

public class OthelloServer extends Thread {

	private final ServerCommandIO serverCommandIO;

	public OthelloServer() {
		this.serverCommandIO = new ServerCommandIO();
		CommandReceiveThread thread = new CommandReceiveThread();
		thread.start();
	}

	@Override
	public void run() {

	}

	// ============================= インナークラス =============================
	// NOTE: なぜインナークラスなのか？
	//  OthelloServerでもスレッドを動かしたいから。
	// =======================================================================
	private class CommandReceiveThread extends Thread {
		@Override
		public void run() {
			ServerCommand command = serverCommandIO.nextServerCommand();
			log("run", command.format());
//			command.executeOn(OthelloServer.this);
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

package controller;

import model.Othello;
import network.MessageServer;

import java.util.Scanner;

public class OthelloServer extends Thread {

	private final ServerCommandIO serverCommandIO;

	public OthelloServer() {
		this.serverCommandIO = new ServerCommandIO();
		CommandRecieveThread thread = new CommandRecieveThread();
//		thread.start();
	}


	// ============================= インナークラス =============================
	// NOTE: なぜインナークラスなのか？
	//  OthelloServerでもスレッドを動かしたいから。
	private class CommandRecieveThread extends Thread {
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

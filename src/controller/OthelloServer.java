package controller;

import network.MessageServer;

import java.util.Scanner;

public class OthelloServer extends Thread {

	private final MessageServer messageServer;

	public OthelloServer() {

		// サーバをポート番号10000で、最大接続数50で起動
		this.messageServer = new MessageServer(10000, 50);

		// メッセージの受け取りは別スレッドで行う

		// 標準入力から受け取ったメッセージをブロードキャストする
		while (true) {
			Scanner sc = new Scanner(System.in);
			String message = sc.nextLine();
			if (message.equals("exit")) break;
			messageServer.broadcast(message);
		}
	}

	@Override
	public void run() {
		while(true) {
			String message = this.messageServer.nextMessage();

		}
	}
}

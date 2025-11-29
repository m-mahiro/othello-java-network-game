package controller.server;

import network.MessageServer;

import java.util.Scanner;

public class OthelloServer {

	public static void main(String[] args) {

		// サーバをポート番号10000で、最大接続数50で起動
		MessageServer messageServer = new MessageServer(10000, 50);

		// メッセージの受け取りは別スレッドで行う
		MessageRecieveThread messageRecieveThread = new MessageRecieveThread(messageServer);
		messageRecieveThread.start();

		// 標準入力から受け取ったメッセージをブロードキャストする
		while (true) {
			Scanner sc = new Scanner(System.in);
			String message = sc.nextLine();
			if (message.equals("exit")) break;
			messageServer.broadcast(message);
		}
	}

	static class MessageRecieveThread extends Thread {
		private final MessageServer messageServer;

		MessageRecieveThread(MessageServer messageServer) {
			this.messageServer = messageServer;
		}

		@Override
		public void run() {
			while (true) {
				String message = messageServer.nextMessage();
				System.out.println(message);
			}
		}
	}

}

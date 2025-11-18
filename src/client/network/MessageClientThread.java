package client.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MessageClientThread extends Thread {

	private BufferedReader in;
	private PrintWriter out;

	public MessageClientThread(String clientName) {
		try {
			Socket socket = new Socket("localhost", 10000);
			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			this.in = new BufferedReader(inputStreamReader);
			this.out = new PrintWriter(socket.getOutputStream());
			this.send(clientName); // 接続して初めの一行はclientName todo: 気に入らない
			String helloMessage = this.waitMessage(); // "Hello, client No.1!"を受け取る
//			System.out.println(helloMessage);
		} catch (UnknownHostException e) {
			System.out.println("ホストのIPアドレスが判定できません。: " + e);
		} catch (IOException e) {
			System.out.println("エラーが発生しました。" + e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void send(String str) {
		out.println(str);
		out.flush();
	}

	public void run () {
		while(true) {
			try {
				String message = this.waitMessage();
				if (message == null) break;
//				System.out.println(message);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public String waitMessage() throws Exception {
		String message = in.readLine();
		if (message == null) return null;
		System.out.println("Srv->ME: " + message);
		return message;
	}


}
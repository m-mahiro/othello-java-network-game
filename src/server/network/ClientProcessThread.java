package server.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientProcessThread extends Thread{

	public int clientId;
	public String name;
	private BufferedReader in;
	private PrintWriter out;


	ClientProcessThread(int clientId, BufferedReader in, PrintWriter out, String clientName) {
		this.clientId = clientId;
		this.in = in;
		this.out = out;
	}

	public void run() {
		try {
			send("Hello, client No." + this.clientId + "!");

			while(true) {
				String message = this.waitMessage();
				if (message == null) break;
				MessageServer.sendAll(message, this);
			}

		} catch (Exception e) {
			MessageServer.terminateClientProcess(this, e);
			throw new RuntimeException(e);
		}
	}

	public void send(String str) {
		out.println(str);
		out.flush();
		System.out.println("Srv->" + clientId + ": " + str);
	}

	public String waitMessage() throws Exception {
		String message = in.readLine();
		if (message == null) return null;
		System.out.println(this.clientId + "->Srv: " + message);
		return message;
	}

}
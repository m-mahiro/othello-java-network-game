package server.network;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class ClientProcessThread extends Thread{

	public int id;
	public String name;
	private BufferedReader in;
	private PrintWriter out;


	ClientProcessThread(int id, BufferedReader in, PrintWriter out, String clientName) {
		this.id = id;
		this.in = in;
		this.out = out;
		this.name = clientName;
	}

	public void run() {
		try {
			send("Hello, client No." + this.id + "!");

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
		System.out.println("Srv->" + id + ": " + str);
	}

	public String waitMessage() throws Exception {
		String message = in.readLine();
		if (message == null) return null;
		System.out.println(this.id + "->Srv: " + message);
		return message;
	}

}
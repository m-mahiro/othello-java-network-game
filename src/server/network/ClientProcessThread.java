package server.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ClientProcessThread {

	public int clientId;
	public String name;
	private BufferedReader in;
	private PrintWriter out;


	ClientProcessThread(int clientId, BufferedReader in, PrintWriter out) {
		this.clientId = clientId;
		this.out = out;
	}

	public void run() {
		try {
			out.println("Hello, client No." + this.clientId + "! Enter 'Bye' to exit.");

			while(true) {
				String str = in.readLine();
				System.out.println("Received from client No." + this.clientId + "(" + this.name + "), Messages: " + str);
			}

		} catch (Exception e) {
			OthelloServer.terminateClientProcess(this);
		}
	}

}
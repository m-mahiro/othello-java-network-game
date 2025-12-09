import controller.OthelloServer;
import network.MessageServer;

public class OthelloServerLauncher {
	public static void main(String[] args) {
		OthelloServer othelloServer = new OthelloServer();
		othelloServer.start();
	}
}

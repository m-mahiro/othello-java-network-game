import controller.OthelloServer;
import network.MessageServer;

public class OthelloServerLauncher {
	public static void main(String[] args) {
		new OthelloServer(new MessageServer(10000, 100));
	}
}

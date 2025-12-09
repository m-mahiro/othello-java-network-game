import controller.OthelloClient;
import network.MessageClient;

// 受信のテスト
public class OthelloClientLauncher1 {
	public static void main(String[] args) {
		OthelloClient othelloClient = new OthelloClient(new MessageClient("localhost", 10000));
		othelloClient.start();

	}
}

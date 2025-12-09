import controller.OthelloClient;
import network.MessageClient;

// 送信のテスト
public class OthelloClientLauncher2 {
	public static void main(String[] args) {
		OthelloClient othelloClient = new OthelloClient();
		othelloClient.start();  // review: start()はコンストラクタに入れるべき?
	}
}

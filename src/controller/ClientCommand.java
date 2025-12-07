package controller;

import com.sun.org.apache.xml.internal.serializer.ToSAXHandler;
import model.Othello;

// クライアントで実行されるコマンド
class ClientCommand {

	private final OthelloClient othello;
	private final ClientCommandType type;
	private final String arguments;

	// hack: エラーハンドリングがばがば
	public ClientCommand(OthelloClient othelloClient, String commandString) {

		this.othello = othelloClient;
		this.type = ClientCommandType.valueOf(commandString.split(" ")[0]);

		// type以降のトークンは全て一つの文字列として扱う
		char[] arr = commandString.toCharArray();
		for (int i = 0; i < commandString.length(); i++) {
			char c = arr[i];
			if (c == ' ')  {
				this.arguments = commandString.substring(i + 1);
				return;
			}
		}

		throw CommandException.invalidCommandFormat(commandString);
	}

	public void execute() {
		type.execute(this.othello, this.arguments);
	}

	public String format() {
		return this.type + " " + this.arguments;
	}

	public static String playWith(int address, String name) {

		return ClientCommandType.PLAY_WITH + " " + address + " " + name;
	}

	public static String putCoin(int i, int j) {
		return ClientCommandType.PUT_COIN + " " + i + " " + j;
	}

	public static String revert() {
		return ClientCommandType.REVERT.toString();
	}

	public static String restart() {
		return ClientCommandType.RESTART.toString();
	}


	// ========================================== インナークラス ================================================
	// NOTE: インナークラスである必要がある理由:
	//     このクラスはClientCommandからしかアクセスできてはいけないから。
	//     インナークラスにした方がパッケージの構造がシンプルになるから。
	// NOTE: ClientCommandからしかアクセスで来てはいけない理由:
	//     単純に想定していないから。
	// =======================================================================================================
	private enum ClientCommandType {

		PLAY_WITH {
			// REVIEW: 名前がおかしきがする。
			// サーバからこのコマンドを受け取ることで、対戦相手の通知を受ける。
			@Override
			void execute(OthelloClient othelloClient, String commandString) {
				// TODO:
			}
		},
		PUT_COIN {
			@Override
			void execute(OthelloClient othelloClient, String commandString) {
				// todo: 未実装
			}
		},
		REVERT {
			@Override
			void execute(OthelloClient othelloClient, String commandString) {

			}
		},
		RESTART {
			@Override
			void execute(OthelloClient othelloClient, String commandString) {

			}
		};

		abstract void execute(OthelloClient othelloClient, String commandString);

	}

}
package controller;

import domain.Othello;

class ClientCommand {

	Othello othello;
	ClientCommandType type;
	String commandString;

	// todo: エラーハンドリングがばがば
	ClientCommand(Othello othello, String message) {

		this.othello = othello;
		this.type = ClientCommandType.valueOf(message.split(" ")[0]);

		// type以降のトークンは全て一つの文字列として扱う
		char[] arr = message.toCharArray();
		for (int i = 0; i < message.length(); i++) {
			char c = arr[i];
			if (c == ' ') this.commandString = message.substring(i + 1);
		}
	}

	public void execute() {
		type.execute(this.othello, this.commandString);
	}

	public static String putCoin(int i, int j) {
		String str = "";
		str += i + " ";
		str += j + " ";
		return str;
	}


	// ========================================== インナークラス ================================================
	// インナークラスである必要がある理由:
	//     このクラスはClientCommandからしかアクセスできてはいけないから。
	//     インナークラスにした方がパッケージの構造がシンプルになるから。
	// ClientCommandからしかアクセスで来てはいけない理由:
	//     単純に想定していないから。
	// =======================================================================================================
	private enum ClientCommandType {

		PUT_COIN {
			@Override
			void execute(Othello othello, String commandString) {
				// todo: 未実装
			}
		};

		abstract void execute(Othello othello, String commandString);

	}

}
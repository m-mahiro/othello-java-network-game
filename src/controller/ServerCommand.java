package controller;

import model.Othello;

class ServerCommand {

	Othello othello;
	ServerCommandType type;
	String commandString;

	// hack: エラーハンドリングがばがば
	ServerCommand(Othello othello, String message) {

		this.othello = othello;
		this.type = ServerCommandType.valueOf(message.split(" ")[0]);

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

	public static String registerProfile(int myAddress, String myName) {
		return ServerCommandType.REGISTER_PROFILE + " " + myAddress + " " + myName;
	}

	public static String searchOpponent(int myAddress) {
		return ServerCommandType.SEARCH_OPPONENT + " " + myAddress;
	}


	// ========================================== インナークラス ================================================
	// NOTE: インナークラスである必要がある理由:
	//     このクラスはClientCommandからしかアクセスできてはいけないから。
	//     インナークラスにした方がパッケージの構造がシンプルになるから。
	// NOTE: ClientCommandからしかアクセスで来てはいけない理由:
	//     単純に想定していないから。
	// =======================================================================================================
	enum ServerCommandType {

		REGISTER_PROFILE {
			@Override
			void execute(Othello othello, String commandString) {
				// 引数: クライアントのアドレス、名前
				// 処理: その情報をOthelloServer内に保存。（キーがアドレスのmap）
			}
		},
		SEARCH_OPPONENT {
			@Override
			void execute(Othello othello, String commandString) {
				// 引数: 試合開始の準備が整ったクライアントのアドレス
				// 処理: このコマンドを送ってきたクライアントにClientCommand.playWith()を送る。
			}
		};

		abstract void execute(Othello othello, String commandString);

	}

}
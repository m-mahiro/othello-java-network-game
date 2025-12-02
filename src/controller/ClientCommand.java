package controller;

import model.Othello;

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

	// ========================================== インナークラス ================================================
	// インナークラスである必要がある理由:
	//     このクラスはClientCommandからしかアクセスできてはいけないから。
	//     インナークラスにした方がパッケージの構造がシンプルになるから。
	// ClientCommandからしかアクセスで来てはいけない理由:
	//     単純に想定していないから。
	// =======================================================================================================
	public enum ClientCommandType {

		DOG {
			@Override
			void execute(Othello othello, String commandString) {

			}
		},
		CAT{
			@Override
			void execute(Othello othello, String commandString) {

			}
		};

		abstract void execute(Othello othello, String commandString);

	}

}
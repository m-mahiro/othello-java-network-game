package controller;

import model.Othello;

import java.util.Objects;

class ClientCommand {

	Othello othello;
	ClientCommandType type;
	String commandString;

	// hack: エラーハンドリングがばがば
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
	// NOTE: インナークラスである必要がある理由:
	//     このクラスはClientCommandからしかアクセスできてはいけないから。
	//     インナークラスにした方がパッケージの構造がシンプルになるから。
	// NOTE: ClientCommandからしかアクセスで来てはいけない理由:
	//     単純に想定していないから。
	// =======================================================================================================
	enum ClientCommandType {

		OPPONENT_PROFILE {
			@Override
			void execute(Othello othello, String commandString) {
				// TODO:
			}
		},
		PUT_COIN {
			@Override
			void execute(Othello othello, String commandString) {
				// todo: 未実装
			}
		},
		REVERT {
			@Override
			void execute(Othello othello, String commandString) {

			}
		},
		RESTART {
			@Override
			void execute(Othello othello, String commandString) {

			}
		};

		ClientCommand() {

		}

		abstract void execute(Othello othello, String commandString);

	}

}
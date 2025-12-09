package controller;

import java.util.Arrays;

class ClientCommand {

	final Type type; // SMELL: フィールドがパブリック(finalだからまだセーフ？)
	private final String[] arguments;

	// hack: エラーハンドリングがばがば
	ClientCommand(String commandString) {
		String[] array = commandString.split(" ");
		Type type = Type.valueOf(array[0]);
		String[] args = Arrays.copyOfRange(array, 1, array.length);
		this.type = type;
		this.arguments = args;
	}

	// hack: エラーハンドリングがばがば
	private ClientCommand(Type type, String[] arguments) {
		this.type = type;
		this.arguments = arguments;
	}

	String format() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.type.toString());
		for (String arg : this.arguments) {
			sb.append(" ");
			sb.append(arg);
		}
		return sb.toString();
	}

	void executeOn(OthelloClient othelloClient) {
		type.execute(othelloClient, this.arguments);
	}


	// ============================= ClientCommandインスタンスを作成するメソッド群 =============================

	static ClientCommand playWith(int address, String name) {
		ClientCommand.Type type = ClientCommand.Type.PLAY_WITH;
		String[] args = {Integer.toString(address), name};
		return new ClientCommand(type, args);
	}

	static ClientCommand putCoin(int i, int j) {
		ClientCommand.Type type = ClientCommand.Type.PUT_COIN;
		String[] args = {Integer.toString(i), Integer.toString(j)};
		return new ClientCommand(type, args);
	}

	static ClientCommand revert() {
		ClientCommand.Type type = ClientCommand.Type.REVERT;
		String[] arg = {};
		return new ClientCommand(type, arg);
	}

	static ClientCommand restart() {
		ClientCommand.Type type = ClientCommand.Type.RESTART;
		String[] arg = {};
		return new ClientCommand(type, arg);
	}


	// ============================= デバッグ用 =============================
	private void log(String method, String string) {
		if (method.equals("()")) {
			System.out.println("[ClientCommand()] " + string);
		} else {
			System.out.println("[ClientCommand" + method + "()] " + string);
		}
	}

	// ========================================== インナークラス ================================================
	// NOTE: Typeクラスのexecute()へは、ClientCommandからしかアクセスされたくない。
	// =======================================================================================================
	private enum Type {

		PLAY_WITH {
			// REVIEW: 名前がおかしきがする。
			// サーバからこのコマンドを受け取ることで、対戦相手の通知を受ける。
			@Override
			void execute(OthelloClient serverClient, String[] args) {
				// TODO:
			}

			@Override
			int getArgumentSize() {
				return 0;
			}
		},
		PUT_COIN {
			@Override
			void execute(OthelloClient serverClient, String[] args) {
				log("execute", "実行されました!");
			}

			@Override
			int getArgumentSize() {
				return 0;
			}
		},
		REVERT {
			@Override
			void execute(OthelloClient serverClient, String[] args) {

			}

			@Override
			int getArgumentSize() {
				return 0;
			}
		},
		RESTART {
			@Override
			void execute(OthelloClient serverClient, String[] args) {

			}

			@Override
			int getArgumentSize() {
				return 0;
			}
		};

		abstract void execute(OthelloClient serverClient, String[] arguments);

		abstract int getArgumentSize();


		// ============================= デバッグ用 =============================
		private static void log(String method, String string) {
			if (method.equals("()")) {
				System.out.println("[ClientCommand.Type()] " + string);
			} else {
				System.out.println("[ClientCommand.Type" + method + "()] " + string);
			}
		}

	}

}

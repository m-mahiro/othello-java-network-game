package controller;

import model.Coin;

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

	static ClientCommand playWith(int address, String name, Coin coin) {
		ClientCommand.Type type = ClientCommand.Type.PLAY_WITH;
		String[] args = {Integer.toString(address), name, coin.name()};
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
			@Override
			void execute(OthelloClient serverClient, String[] args) {
				int address = Integer.parseInt(args[0]);
				String name = args[1];
				Coin coin = Coin.valueOf(args[2]);
				log("execute", "相手が見つかりました！ (address: " + address + ", coin: " + coin + ")");
			}

			@Override
			int getArgumentSize() {
				return 3;
			}
		},

		PUT_COIN {
			@Override
			void execute(OthelloClient serverClient, String[] args) {
				int i = Integer.parseInt(args[0]);
				int j = Integer.parseInt(args[1]);
				log("execute", "コインを置こう!(i: " + i + ", j:" + j + ")");
			}

			@Override
			int getArgumentSize() {
				return 2;
			}
		},

		REVERT {
			@Override
			void execute(OthelloClient serverClient, String[] args) {
				log("execute", "一個戻そう");
			}

			@Override
			int getArgumentSize() {
				return 0;
			}
		},

		RESTART {
			@Override
			void execute(OthelloClient serverClient, String[] args) {
				log("execute", "再スタートしよう!");
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

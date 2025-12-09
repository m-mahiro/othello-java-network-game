package controller;

import java.util.Arrays;

class ServerCommand {

	private final Type type;
	private final String[] arguments;

	// hack: エラーハンドリングがばがば
	ServerCommand(String commandString) {
		String[] array = commandString.split(" ");
		Type type = Type.valueOf(array[0]);
		String[] args = Arrays.copyOfRange(array, 1, array.length);
		this.type = type;
		this.arguments = args;
	}

	// hack: エラーハンドリングがばがば
	private ServerCommand(Type type, String[] arguments) {
		this.type = type;
		this.arguments = arguments;
	}

	public String format() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.type.toString());
		for (String arg : this.arguments) {
			sb.append(" ");
			sb.append(arg);
		}
		return sb.toString();
	}

	public void executeOn(OthelloServer serverClient) {
		type.execute(serverClient, this.arguments);
	}


	// ============================= ServerCommandインスタンスを作成するメソッド群 =============================
	public static ServerCommand registerClient(int address, String name) {
		ServerCommand.Type type = ServerCommand.Type.REGISTER_CLIENT;
		String[] args = {Integer.toString(address), name};
		return new ServerCommand(type, args); // HACK: コマンドの引数に関する知識が、CommandクラスとCommanderとに散らばっている。
	}

	public static ServerCommand searchOpponent() {
		ServerCommand.Type type = ServerCommand.Type.SEARCH_OPPONENT;
		String[] args = {};
		return new ServerCommand(type, args);
	}



	// ========================================== インナークラス ================================================
	// NOTE: Typeクラスのexecute()へは、ServerCommandクラスからしかアクセスされたくない。
	// 外部にexecute()を公開してしまうと、String[] argumentsの知識を前提に使わせることになるから危ない。
	// =======================================================================================================
	enum Type {

		REGISTER_CLIENT {
			@Override
			void execute(OthelloServer othelloServer, String[] arguments) {

			}
		},
		SEARCH_OPPONENT {
			@Override
			void execute(OthelloServer othelloServer, String[] arguments) {

			}
		};

		abstract void execute(OthelloServer othelloServer, String[] arguments);
	}

}


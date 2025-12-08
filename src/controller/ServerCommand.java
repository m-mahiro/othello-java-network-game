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

	ServerCommand(Type type, String[] arguments) {
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

	public void executeOn(OthelloClient serverClient) {
		type.execute(serverClient, this.arguments);
	}


	// ============================= ServerCommandインスタンスを作成するメソッド群 =============================
	public ServerCommand registerClient(int address, String name) {
		ServerCommand.Type type = ServerCommand.Type.REGISTER_CLIENT;
		String[] args = {Integer.toString(address), name};
		return new ServerCommand(type, args); // HACK: コマンドの引数に関する知識が、CommandクラスとCommanderとに散らばっている。
	}

	public ServerCommand searchOpponent() {
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
			void execute(OthelloClient serverClient, String[] arguments) {

			}

			@Override
			int getArgumentSize() {
				return 0;
			}
		},
		SEARCH_OPPONENT {
			@Override
			void execute(OthelloClient serverClient, String[] arguments) {

			}

			@Override
			int getArgumentSize() {
				return 0;
			}
		};

		abstract void execute(OthelloClient serverClient, String[] arguments);
		abstract int getArgumentSize();
	}

}


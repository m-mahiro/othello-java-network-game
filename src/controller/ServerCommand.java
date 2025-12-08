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


	// ========================================== インナークラス ================================================
	// NOTE: 間違ってもstaticクラスにしてはいけない、Typeだけでインスタンスを作成してexecute()できしまうから。
	// HACK: ↑なんかexecute()できてしまっているけど、一旦無視。
	// NOTE: CommandIO内で、コマンドをタイプごとに振り分けるのでプライベートクラスではだめ。（パッケージプライベートにする）
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


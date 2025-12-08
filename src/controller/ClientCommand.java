package controller;

import java.util.Arrays;

class ClientCommand {

	public final Type type; // SMELL: フィールドがパブリック(finalだからまだセーフ？)
	private final String[] arguments;

	// hack: エラーハンドリングがばがば
	ClientCommand(String commandString) {
		String[] array = commandString.split(" ");
		Type type = Type.valueOf(array[0]);
		String[] args = Arrays.copyOfRange(array, 1, array.length);
		this.type = type;
		this.arguments = args;
	}

	ClientCommand(Type type, String[] arguments) {
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
	// NOTE: CommandIO内で、コマンドをタイプごとに振り分けるのでプライベートクラスではだめ。（パッケージプライベートにする）
	// =======================================================================================================
	enum Type {

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
				// todo: 未実装
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
	}

}

package controller;

import network.MessageClient;

import java.util.Arrays;

class ClientCommander {

	private final MessageClient messageClient;
	private final int targetAddress;

	public ClientCommander(MessageClient messageClient, int targetAddress) {
		this.messageClient = messageClient;
		this.targetAddress = targetAddress;
	}


	public void playWith(int address, String name) {
		ClientCommandType type = ClientCommandType.PLAY_WITH;
		String[] args = {Integer.toString(address), name};
		ClientCommand command =  new ClientCommand(type, args);
		send(command);
	}

	public void putCoin(int i, int j) {
		ClientCommandType type = ClientCommandType.PUT_COIN;
		String[] args = {Integer.toString(i), Integer.toString(j)};
		ClientCommand command =  new ClientCommand(type, args);
		send(command);
	}

	public void revert() {
		ClientCommandType type = ClientCommandType.REVERT;
		String[] arg = {};
		ClientCommand command =  new ClientCommand(type, arg);
		send(command);
	}

	public void restart() {
		ClientCommandType type = ClientCommandType.RESTART;
		String[] arg = {};
		ClientCommand command =  new ClientCommand(type, arg);
		send(command);
	}

	private void send(ClientCommand command) {
		messageClient.sendToServer(command.format());
	}


	// ============================= インナークラス =============================
	// クライアントで実行されるコマンド
	public class ClientCommand {

		private final ClientCommandType type;
		private final String[] arguments;

		// hack: エラーハンドリングがばがば
		public ClientCommand(String commandString) {
			String[] array = commandString.split(" ");
			ClientCommandType type = ClientCommandType.valueOf(array[0]);
			String[] args = Arrays.copyOfRange(array, 1, array.length);
			this.type = type;
			this.arguments = args;
		}

		private ClientCommand(ClientCommandType type, String[] arguments) {
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

	}

	// ========================================== インナークラス ================================================
	// NOTE: インナークラスである必要がある理由:
	//     このクラスはClientCommandからしかアクセスできてはいけないから。
	//     インナークラスにした方がパッケージの構造がシンプルになるから。
	// NOTE: ClientCommandからしかアクセスで来てはいけない理由:
	//     単純に想定していないから。
	// =======================================================================================================
	private enum ClientCommandType {

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

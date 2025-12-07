package controller;

import network.MessageClient;

import java.util.Arrays;

class ServerCommander {

	private final MessageClient messageClient;
	private final int targetAddress;

	public ServerCommander(MessageClient messageClient, int targetAddress) {
		this.messageClient = messageClient;
		this.targetAddress = targetAddress;
	}

	public void registerClient(int address, String name) {
		ServerCommandType type = ServerCommandType.REGISTER_CLIENT;
		String[] args = {Integer.toString(address), name};
		ServerCommand command = new ServerCommand(type, args);
	}

	public void searchOpponent() {
		ServerCommandType type = ServerCommandType.SEARCH_OPPONENT;
		String[] args = {};
		ServerCommand command = new ServerCommand(type, args);
	}

	private void send(ServerCommand command) {
		messageClient.send(targetAddress, command.format());
	}


	// ============================= インナークラス =============================
	// クライアントで実行されるコマンド
	public static class ServerCommand {

		private final ServerCommandType type;
		private final String[] arguments;

		// hack: エラーハンドリングがばがば
		public ServerCommand(String commandString) {
			String[] array = commandString.split(" ");
			ServerCommandType type = ServerCommandType.valueOf(array[0]);
			String[] args = Arrays.copyOfRange(array, 1, array.length);
			this.type = type;
			this.arguments = args;
		}

		private ServerCommand(ServerCommandType type, String[] arguments) {
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
	//     このクラスはServerCommandからしかアクセスできてはいけないから。
	//     インナークラスにした方がパッケージの構造がシンプルになるから。
	// NOTE: ServerCommandからしかアクセスで来てはいけない理由:
	//     単純に想定していないから。
	// =======================================================================================================
	private enum ServerCommandType {

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

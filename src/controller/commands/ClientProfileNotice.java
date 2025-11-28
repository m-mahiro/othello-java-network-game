package controller.commands;

public class ClientProfileNotice implements Command {

	public static final CommandType type = CommandType.CLIENT_PROFILE;
	public final String clientName;

	private static final int headerSize = 1;

	public ClientProfileNotice(String clientName) {
		this.clientName = clientName;
	}

	public static ClientProfileNotice parse(String commandString) {

		CommandType type;
		String name;

		String[] args = commandString.split(" ");

		// コマンドタイプのエラーハンドリング
		try {
			type = Command.getTypeFrom(commandString);
			if (type != ClientProfileNotice.type) throw CommandException.illegalCommandType(type);
		} catch (IllegalArgumentException e) {
			throw CommandException.noSuchCommandType(args[0]);
		}

		char[] charArray = commandString.toCharArray();
		int count = 0;
		int bodyIndex = -1;

		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] == ' ') {
				count++;
			}
			if (count == headerSize) {
				bodyIndex = i;
				break;
			}
		}
		if (count != headerSize) throw CommandException.invalidCommandFormat(commandString);
		name = commandString.substring(bodyIndex + 1);

		return new ClientProfileNotice(name);
	}

	@Override
	public String format() {
		String str = "";
		str += ClientProfileNotice.type.toString() + " ";
		str += clientName;
		return str;
	}

	@Override
	public CommandType getType() {
		return ClientProfileNotice.type;
	}

	@Override
	public String toString() {
		return format();
	}

}
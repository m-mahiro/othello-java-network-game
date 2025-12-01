package controller.commands;

public interface Command {

	String format();

	CommandType getType();

	// todo: 本当に要るかどうかは要検討
	static CommandType getTypeFrom(String commandString) {
		String[] args = commandString.split(" ");
		try {
			return CommandType.valueOf(args[0]);
		} catch (IllegalArgumentException e) {
			throw CommandException.noSuchCommandType(commandString);
		}
	}
}

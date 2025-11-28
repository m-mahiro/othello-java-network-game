package controller.commands;

public interface Command {

	String format();

	CommandType getType();

	static CommandType getTypeFrom(String commandString) {
		String[] args = commandString.split(" ");
		try {
			return CommandType.valueOf(args[0]);
		} catch (IllegalArgumentException e) {
			throw CommandException.noSuchCommandType(commandString);
		}
	}

}

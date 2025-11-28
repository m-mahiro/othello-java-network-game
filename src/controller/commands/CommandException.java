package controller.commands;

public class CommandException extends RuntimeException {

	public CommandException(String command) {
		super(command);
	}

	public static CommandException noSuchCommandType(String commandString) {
		return new CommandException("No such command type: " + commandString); // todo: 英語がおかしい
	}

	public static CommandException invalidCommandFormat(String commandString) {
		return new CommandException("Invalid command format: " + commandString);
	}

	public static CommandException illegalCommandType(CommandType type) {
		return new CommandException("Illegal command type: " + type.toString());
	}

	public static CommandException unsupportedCommandType(CommandType type) {
		return new CommandException("Unsupported command type: " + type);
	}

}

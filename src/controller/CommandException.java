package controller;

public class CommandException extends RuntimeException {
	public CommandException(String message) {
		super(message);
	}

	public static CommandException noSuchCommandType(String commandString) {
		return new CommandException("No such command type: " + commandString); // todo: 英語がおかしい
	}

	public static CommandException invalidCommandFormat(String commandString) {
		return new CommandException("Invalid command format: " + commandString);
	}

}

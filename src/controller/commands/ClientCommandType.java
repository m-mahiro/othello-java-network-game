package controller.commands;

import model.Othello;

public enum ClientCommandType {

	DOG {
		@Override
		void execute(Othello othello, String commandString) {

		}
	},
	CAT{
		@Override
		void execute(Othello othello, String commandString) {

		}
	};

	abstract void execute(Othello othello, String commandString);

}
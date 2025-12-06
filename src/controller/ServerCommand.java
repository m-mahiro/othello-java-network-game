package controller;

import model.Othello;

public enum ServerCommand {
	MY_PROFILE {
		@Override
		void execute(Othello othello, String commandString) {

		}
	};

	abstract void execute(Othello othello, String commandString);

}

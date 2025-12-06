package model;

public class OthelloModelException extends Exception {
	public OthelloModelException(String message) {
		super(message);
	}

	// hack: とりあえず引数全部なしで。

	public static OthelloModelException alreadyExistsCoin() {
		return new OthelloModelException("A Coin has already exited");
	}

	public static OthelloModelException mustNotBeNONE() {
		return new OthelloModelException("The coin must not be NONE here.");
	}

	public static OthelloModelException cannotPutCoin() {
		return new OthelloModelException("Cannot put a coin there");
	}

	public static OthelloModelException noSuchCell() {
		return new OthelloModelException("No such cell");
	}

	public static OthelloModelException invalidNoneCoin() {
		return new OthelloModelException("NONE coin is invalid");
	}

	public static OthelloModelException isNotFinished() {return new OthelloModelException("The Othello game haven't finished");}

}


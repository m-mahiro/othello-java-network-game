package model;

public class OthelloModelException extends Exception {
	public OthelloModelException(String message) {
		super(message);
	}

	public static OthelloModelException alreadyExistsCoin() {
		return new OthelloModelException("A Coin has already exited");
	}

	public static OthelloModelException noCoin() {
		return new OthelloModelException("No coin exits");
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
}

